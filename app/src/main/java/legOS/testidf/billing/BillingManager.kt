package legOS.testidf.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Менеджер для управления подписками через Google Play Billing
 *
 * ОБНОВЛЕНО: Поддержка двух типов подписок (месячная и годовая)
 */
class BillingManager(private val context: Context) : PurchasesUpdatedListener {

    companion object {
        private const val TAG = "BillingManager"

        // ID годовой подписки
        const val SUBSCRIPTION_ANNUAL_PRODUCT_ID = "annual_subscription_main_1.0"

        // ID месячной подписки
        const val SUBSCRIPTION_MONTHLY_PRODUCT_ID = "monthly_subscription_main_1.0"

        // Файл для сохранения статуса подписки
        private const val SUBSCRIPTION_CACHE_FILE = "subscription_status.txt"
    }

    private var billingClient: BillingClient? = null

    // CoroutineScope для запуска suspend функций
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _subscriptionState = MutableStateFlow<SubscriptionState>(SubscriptionState.Loading)
    val subscriptionState: StateFlow<SubscriptionState> = _subscriptionState

    private val _purchaseFlow = MutableStateFlow<PurchaseResult?>(null)
    val purchaseFlow: StateFlow<PurchaseResult?> = _purchaseFlow

    sealed class SubscriptionState {
        object Loading : SubscriptionState()
        data class Active(val subscriptionType: String) : SubscriptionState() // "monthly" или "annual"
        data class Inactive(val reason: String) : SubscriptionState()
        data class Error(val message: String) : SubscriptionState()
        object TestMode : SubscriptionState()
    }

    sealed class PurchaseResult {
        object Success : PurchaseResult()
        data class Error(val message: String) : PurchaseResult()
        object Cancelled : PurchaseResult()
    }

    /**
     * Инициализация Billing Client
     */
    fun initialize() {
        Log.d(TAG, "========================================")
        Log.d(TAG, "Initializing BillingManager")
        Log.d(TAG, "========================================")

        logDeviceInfo()

        // Проверяем эмулятор
        if (isRunningOnEmulator()) {
            Log.d(TAG, "🟢 EMULATOR DETECTED - granting free access")
            _subscriptionState.value = SubscriptionState.TestMode
            saveCachedSubscriptionStatus(true, "test")
            return
        }

        // Проверяем Google Play Services
        if (!isGooglePlayServicesAvailable()) {
            Log.d(TAG, "🟢 GOOGLE PLAY SERVICES NOT AVAILABLE - granting free access (development mode)")
            _subscriptionState.value = SubscriptionState.TestMode
            saveCachedSubscriptionStatus(true, "test")
            return
        }

        // Инициализируем Billing
        try {
            billingClient = BillingClient.newBuilder(context)
                .setListener(this)
                .enablePendingPurchases()
                .build()

            billingClient?.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    Log.d(TAG, "Billing setup finished with code: ${billingResult.responseCode}")

                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        Log.d(TAG, "✅ Billing client connected successfully")
                        coroutineScope.launch {
                            checkSubscriptionStatus()
                        }
                    } else {
                        Log.e(TAG, "❌ Billing setup failed: ${billingResult.debugMessage}")
                        Log.e(TAG, "Response code: ${billingResult.responseCode}")

                        when (billingResult.responseCode) {
                            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE,
                            BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE -> {
                                Log.d(TAG, "🟢 Billing unavailable - granting TEST MODE access")
                                _subscriptionState.value = SubscriptionState.TestMode
                                saveCachedSubscriptionStatus(true, "test")
                            }
                            else -> {
                                val cached = getCachedSubscriptionStatus()
                                if (cached != null) {
                                    Log.d(TAG, "Using cached subscription status: ${cached.first}")
                                    _subscriptionState.value = SubscriptionState.Active(cached.second)
                                } else {
                                    _subscriptionState.value = SubscriptionState.Error(
                                        "Impossible de vérifier l'abonnement. Vérifiez votre connexion Internet."
                                    )
                                }
                            }
                        }
                    }
                }

                override fun onBillingServiceDisconnected() {
                    Log.w(TAG, "⚠️ Billing service disconnected")
                    val cached = getCachedSubscriptionStatus()
                    if (cached != null) {
                        _subscriptionState.value = SubscriptionState.Active(cached.second)
                    } else {
                        _subscriptionState.value = SubscriptionState.TestMode
                    }
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "❌ Exception during billing initialization", e)
            _subscriptionState.value = SubscriptionState.TestMode
            saveCachedSubscriptionStatus(true, "test")
        }
    }

    /**
     * Логирование информации об устройстве
     */
    private fun logDeviceInfo() {
        Log.d(TAG, "=== Device Information ===")
        Log.d(TAG, "MANUFACTURER: ${android.os.Build.MANUFACTURER}")
        Log.d(TAG, "MODEL: ${android.os.Build.MODEL}")
        Log.d(TAG, "PRODUCT: ${android.os.Build.PRODUCT}")
        Log.d(TAG, "=========================")
    }

    /**
     * Проверка статуса подписки (обе подписки)
     */
    suspend fun checkSubscriptionStatus() = withContext(Dispatchers.IO) {
        Log.d(TAG, "=== Checking subscription status ===")

        if (isRunningOnEmulator()) {
            Log.d(TAG, "🟢 EMULATOR detected - free access granted")
            _subscriptionState.value = SubscriptionState.TestMode
            saveCachedSubscriptionStatus(true, "test")
            return@withContext
        }

        val client = billingClient
        if (client == null || !client.isReady) {
            Log.e(TAG, "❌ Billing client not ready")

            val cached = getCachedSubscriptionStatus()
            if (cached != null) {
                Log.d(TAG, "Using cached status: ${cached.first}")
                _subscriptionState.value = SubscriptionState.Active(cached.second)
            } else {
                Log.d(TAG, "🟢 Development mode - granting test access")
                _subscriptionState.value = SubscriptionState.TestMode
                saveCachedSubscriptionStatus(true, "test")
            }
            return@withContext
        }

        try {
            Log.d(TAG, "📦 Querying product details...")

            // Запрашиваем обе подписки
            val productParams = QueryProductDetailsParams.newBuilder()
                .setProductList(
                    listOf(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(SUBSCRIPTION_ANNUAL_PRODUCT_ID)
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build(),
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(SUBSCRIPTION_MONTHLY_PRODUCT_ID)
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build()
                    )
                )
                .build()

            val productResult = client.queryProductDetails(productParams)

            Log.d(TAG, "Product query result: ${productResult.billingResult.responseCode}")
            Log.d(TAG, "Debug message: ${productResult.billingResult.debugMessage}")
            Log.d(TAG, "Products found: ${productResult.productDetailsList?.size ?: 0}")

            productResult.productDetailsList?.forEachIndexed { index, product ->
                Log.d(TAG, "Product $index: ${product.productId} - ${product.name}")
            }

            // Проверяем активные покупки
            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()

            val purchasesResult = client.queryPurchasesAsync(params)

            Log.d(TAG, "Query purchases result code: ${purchasesResult.billingResult.responseCode}")
            Log.d(TAG, "Purchases found: ${purchasesResult.purchasesList.size}")

            // Ищем любую активную подписку (месячную или годовую)
            val activePurchase = purchasesResult.purchasesList.find { purchase ->
                (purchase.products.contains(SUBSCRIPTION_ANNUAL_PRODUCT_ID) ||
                        purchase.products.contains(SUBSCRIPTION_MONTHLY_PRODUCT_ID)) &&
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED
            }

            if (activePurchase != null) {
                Log.d(TAG, "✅ Active subscription found!")
                Log.d(TAG, "   Products: ${activePurchase.products}")
                Log.d(TAG, "   Purchase token: ${activePurchase.purchaseToken.take(20)}...")
                Log.d(TAG, "   Order ID: ${activePurchase.orderId}")
                Log.d(TAG, "   Acknowledged: ${activePurchase.isAcknowledged}")

                // Определяем тип подписки
                val subscriptionType = when {
                    activePurchase.products.contains(SUBSCRIPTION_MONTHLY_PRODUCT_ID) -> "monthly"
                    activePurchase.products.contains(SUBSCRIPTION_ANNUAL_PRODUCT_ID) -> "annual"
                    else -> "unknown"
                }

                Log.d(TAG, "   Subscription type: $subscriptionType")

                if (!activePurchase.isAcknowledged) {
                    acknowledgePurchase(activePurchase)
                }

                _subscriptionState.value = SubscriptionState.Active(subscriptionType)
                saveCachedSubscriptionStatus(true, subscriptionType)
            } else {
                if (isLicenseTester()) {
                    Log.d(TAG, "🟢 LICENSE TESTER detected - free access granted")
                    _subscriptionState.value = SubscriptionState.TestMode
                    saveCachedSubscriptionStatus(true, "test")
                } else {
                    Log.d(TAG, "⚠️ No active subscription found")
                    _subscriptionState.value = SubscriptionState.Inactive(
                        "Aucun abonnement actif trouvé"
                    )
                    saveCachedSubscriptionStatus(false, "none")
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error checking subscription", e)
            e.printStackTrace()

            val cached = getCachedSubscriptionStatus()
            if (cached != null) {
                Log.d(TAG, "Using cached status due to error")
                _subscriptionState.value = SubscriptionState.Active(cached.second)
            } else {
                Log.d(TAG, "🟢 Error fallback - granting test access")
                _subscriptionState.value = SubscriptionState.TestMode
                saveCachedSubscriptionStatus(true, "test")
            }
        }
    }

    /**
     * Запуск процесса покупки
     * @param productId - ID подписки (SUBSCRIPTION_ANNUAL_PRODUCT_ID или SUBSCRIPTION_MONTHLY_PRODUCT_ID)
     */
    suspend fun launchSubscriptionFlow(
        activity: Activity,
        productId: String = SUBSCRIPTION_ANNUAL_PRODUCT_ID
    ) = withContext(Dispatchers.Main) {
        val client = billingClient
        if (client == null || !client.isReady) {
            _purchaseFlow.value = PurchaseResult.Error(
                "Service d'abonnement non disponible"
            )
            return@withContext
        }

        try {
            Log.d(TAG, "========================================")
            Log.d(TAG, "🚀 Starting subscription flow for: $productId")

            // Получаем детали продукта
            val productList = listOf(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(productId)
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            )

            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build()

            val productDetailsResult = withContext(Dispatchers.IO) {
                client.queryProductDetails(params)
            }

            Log.d(TAG, "Product details result code: ${productDetailsResult.billingResult.responseCode}")

            val productDetails = productDetailsResult.productDetailsList?.firstOrNull()

            if (productDetails == null) {
                Log.e(TAG, "❌ Product details not found for: $productId")
                _purchaseFlow.value = PurchaseResult.Error(
                    "Abonnement non disponible"
                )
                return@withContext
            }

            Log.d(TAG, "✅ Product details found: ${productDetails.productId}")

            val offers = productDetails.subscriptionOfferDetails
            Log.d(TAG, "Available offers: ${offers?.size ?: 0}")

            offers?.forEachIndexed { index, offer ->
                Log.d(TAG, "  Offer $index:")
                Log.d(TAG, "    - basePlanId: ${offer.basePlanId}")
                Log.d(TAG, "    - offerId: ${offer.offerId ?: "(empty)"}")
                Log.d(TAG, "    - offerToken: ${offer.offerToken.take(20)}...")
            }

            // Выбираем первый offer (backward compatible)
            val selectedOffer = offers?.firstOrNull { offer ->
                offer.offerId.isNullOrEmpty()
            } ?: offers?.firstOrNull()

            if (selectedOffer == null) {
                Log.e(TAG, "❌ No subscription offers found")
                _purchaseFlow.value = PurchaseResult.Error(
                    "Offres d'abonnement non disponibles"
                )
                return@withContext
            }

            val offerToken = selectedOffer.offerToken
            Log.d(TAG, "✅ Selected offer:")
            Log.d(TAG, "   Base plan: ${selectedOffer.basePlanId}")
            Log.d(TAG, "   Offer ID: ${selectedOffer.offerId ?: "(empty - backward compatible)"}")
            Log.d(TAG, "   Token: ${offerToken.take(20)}...")

            // Запускаем flow покупки
            val flowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(
                    listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(productDetails)
                            .setOfferToken(offerToken)
                            .build()
                    )
                )
                .build()

            val billingResult = client.launchBillingFlow(activity, flowParams)

            Log.d(TAG, "Billing flow result: ${billingResult.responseCode}")
            Log.d(TAG, "========================================")

            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                Log.e(TAG, "❌ Failed to launch billing flow: ${billingResult.debugMessage}")
                _purchaseFlow.value = PurchaseResult.Error(
                    "Erreur lors du lancement de l'achat: ${billingResult.debugMessage}"
                )
            }

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error launching subscription flow", e)
            e.printStackTrace()

            _purchaseFlow.value = PurchaseResult.Error(
                "Erreur: ${e.message}"
            )
        }
    }

    /**
     * Callback при обновлении покупок
     */
    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        Log.d(TAG, "onPurchasesUpdated: ${billingResult.responseCode}")

        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { purchase ->
                    if (purchase.products.contains(SUBSCRIPTION_ANNUAL_PRODUCT_ID) ||
                        purchase.products.contains(SUBSCRIPTION_MONTHLY_PRODUCT_ID)) {
                        handlePurchase(purchase)
                    }
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.d(TAG, "User cancelled purchase")
                _purchaseFlow.value = PurchaseResult.Cancelled
            }
            else -> {
                Log.e(TAG, "Purchase failed: ${billingResult.debugMessage}")
                _purchaseFlow.value = PurchaseResult.Error(
                    "Erreur lors de l'achat: ${billingResult.debugMessage}"
                )
            }
        }
    }

    /**
     * Обработка успешной покупки
     */
    private fun handlePurchase(purchase: Purchase) {
        Log.d(TAG, "Handling purchase: ${purchase.orderId}")
        Log.d(TAG, "Products: ${purchase.products}")

        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                acknowledgePurchase(purchase)
            }

            // Определяем тип подписки
            val subscriptionType = when {
                purchase.products.contains(SUBSCRIPTION_MONTHLY_PRODUCT_ID) -> "monthly"
                purchase.products.contains(SUBSCRIPTION_ANNUAL_PRODUCT_ID) -> "annual"
                else -> "unknown"
            }

            Log.d(TAG, "Purchase type: $subscriptionType")

            _subscriptionState.value = SubscriptionState.Active(subscriptionType)
            _purchaseFlow.value = PurchaseResult.Success
            saveCachedSubscriptionStatus(true, subscriptionType)
        }
    }

    /**
     * Подтверждение покупки
     */
    private fun acknowledgePurchase(purchase: Purchase) {
        val client = billingClient ?: return

        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        client.acknowledgePurchase(params) { billingResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d(TAG, "Purchase acknowledged successfully")
            } else {
                Log.e(TAG, "Failed to acknowledge purchase: ${billingResult.debugMessage}")
            }
        }
    }

    /**
     * Проверка, запущено ли приложение на эмуляторе
     */
    private fun isRunningOnEmulator(): Boolean {
        val isEmulator = (android.os.Build.FINGERPRINT.startsWith("generic")
                || android.os.Build.FINGERPRINT.startsWith("unknown")
                || android.os.Build.FINGERPRINT.contains("test-keys")
                || android.os.Build.MODEL.contains("google_sdk")
                || android.os.Build.MODEL.contains("Emulator")
                || android.os.Build.MODEL.contains("Android SDK built for")
                || android.os.Build.MANUFACTURER.contains("Genymotion")
                || android.os.Build.HARDWARE.contains("goldfish")
                || android.os.Build.HARDWARE.contains("ranchu")
                || android.os.Build.PRODUCT.contains("sdk")
                || android.os.Build.PRODUCT.contains("google_sdk")
                || android.os.Build.PRODUCT.contains("sdk_google")
                || android.os.Build.PRODUCT.contains("sdk_x86")
                || android.os.Build.PRODUCT.contains("vbox86p")
                || android.os.Build.PRODUCT.contains("emulator")
                || android.os.Build.PRODUCT.contains("simulator")
                || (android.os.Build.BRAND.startsWith("generic") && android.os.Build.DEVICE.startsWith("generic"))
                || "google_sdk" == android.os.Build.PRODUCT)

        Log.d(TAG, "Emulator check result: $isEmulator")
        return isEmulator
    }

    /**
     * Проверка наличия Google Play Services
     */
    private fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
        val isAvailable = resultCode == ConnectionResult.SUCCESS

        Log.d(TAG, "Google Play Services availability: $isAvailable (code: $resultCode)")

        return isAvailable
    }

    /**
     * Проверка, является ли пользователь тестировщиком по лицензии
     */
    private fun isLicenseTester(): Boolean {
        return false
    }

    /**
     * Сохранение кэша статуса подписки
     * @param subscriptionType - "monthly", "annual", "test", или "none"
     */
    private fun saveCachedSubscriptionStatus(isActive: Boolean, subscriptionType: String) {
        try {
            val file = File(context.getExternalFilesDir(null), SUBSCRIPTION_CACHE_FILE)
            val timestamp = System.currentTimeMillis()
            file.writeText("$isActive,$subscriptionType,$timestamp")
            Log.d(TAG, "Subscription status cached: active=$isActive, type=$subscriptionType")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to cache subscription status", e)
        }
    }

    /**
     * Получение кэшированного статуса подписки (действителен 7 дней)
     * @return Pair(isActive, subscriptionType) или null
     */
    private fun getCachedSubscriptionStatus(): Pair<Boolean, String>? {
        try {
            val file = File(context.getExternalFilesDir(null), SUBSCRIPTION_CACHE_FILE)
            if (!file.exists()) return null

            val content = file.readText().split(",")
            if (content.size != 3) return null

            val isActive = content[0].toBoolean()
            val subscriptionType = content[1]
            val timestamp = content[2].toLong()
            val currentTime = System.currentTimeMillis()
            val daysPassed = (currentTime - timestamp) / (1000 * 60 * 60 * 24)

            return if (isActive && daysPassed < 7) {
                Pair(isActive, subscriptionType)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to read cached subscription status", e)
            return null
        }
    }

    /**
     * Очистка ресурсов
     */
    fun destroy() {
        billingClient?.endConnection()
        billingClient = null
        Log.d(TAG, "BillingManager destroyed")
    }
}