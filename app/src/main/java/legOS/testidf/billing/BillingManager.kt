package legOS.testidf.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
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
 * ВАЖНО: Поддерживает три режима доступа:
 * 1. Закрытое тестирование (license testers) - бесплатный доступ
 * 2. Эмулятор Android Studio - бесплатный доступ для разработки
 * 3. Production - требуется активная подписка
 */
class BillingManager(private val context: Context) : PurchasesUpdatedListener {

    companion object {
        private const val TAG = "BillingManager"

        // ID продукта подписки (ЗАМЕНИТЕ на ваш реальный ID из Google Play Console)
        const val SUBSCRIPTION_PRODUCT_ID = "tanks_hunter_annual_subscription"

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
        object Active : SubscriptionState()
        data class Inactive(val reason: String) : SubscriptionState()
        data class Error(val message: String) : SubscriptionState()
        object TestMode : SubscriptionState() // Для тестировщиков и эмулятора
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
        Log.d(TAG, "Initializing BillingManager...")

        // Проверяем, запущено ли на эмуляторе
        if (isRunningOnEmulator()) {
            Log.d(TAG, "🟢 Running on EMULATOR - granting free access")
            _subscriptionState.value = SubscriptionState.TestMode
            saveCachedSubscriptionStatus(true)
            return
        }

        billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()

        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "✅ Billing client connected successfully")
                    // ИСПРАВЛЕНИЕ: Запускаем suspend функцию в корутине
                    coroutineScope.launch {
                        checkSubscriptionStatus()
                    }
                } else {
                    Log.e(TAG, "❌ Billing setup failed: ${billingResult.debugMessage}")

                    // Если не удалось подключиться, проверяем кэш
                    if (getCachedSubscriptionStatus()) {
                        Log.d(TAG, "Using cached subscription status: ACTIVE")
                        _subscriptionState.value = SubscriptionState.Active
                    } else {
                        _subscriptionState.value = SubscriptionState.Error(
                            "Impossible de vérifier l'abonnement. Vérifiez votre connexion Internet."
                        )
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.w(TAG, "⚠️ Billing service disconnected")
                // Используем кэшированный статус при отключении
                if (getCachedSubscriptionStatus()) {
                    _subscriptionState.value = SubscriptionState.Active
                }
            }
        })
    }

    /**
     * Проверка статуса подписки
     */
    suspend fun checkSubscriptionStatus() = withContext(Dispatchers.IO) {
        Log.d(TAG, "=== Checking subscription status ===")

        // Проверка на эмулятор
        if (isRunningOnEmulator()) {
            Log.d(TAG, "🟢 EMULATOR detected - free access granted")
            _subscriptionState.value = SubscriptionState.TestMode
            saveCachedSubscriptionStatus(true)
            return@withContext
        }

        val client = billingClient
        if (client == null || !client.isReady) {
            Log.e(TAG, "❌ Billing client not ready")

            // Fallback на кэш
            if (getCachedSubscriptionStatus()) {
                Log.d(TAG, "Using cached status: ACTIVE")
                _subscriptionState.value = SubscriptionState.Active
            } else {
                _subscriptionState.value = SubscriptionState.Error(
                    "Service d'abonnement non disponible"
                )
            }
            return@withContext
        }

        try {
            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()

            val purchasesResult = client.queryPurchasesAsync(params)

            Log.d(TAG, "Query result code: ${purchasesResult.billingResult.responseCode}")
            Log.d(TAG, "Purchases found: ${purchasesResult.purchasesList.size}")

            // Проверяем наличие активной подписки
            val activePurchase = purchasesResult.purchasesList.find { purchase ->
                purchase.products.contains(SUBSCRIPTION_PRODUCT_ID) &&
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED
            }

            if (activePurchase != null) {
                Log.d(TAG, "✅ Active subscription found!")

                // Подтверждаем покупку, если еще не подтверждена
                if (!activePurchase.isAcknowledged) {
                    acknowledgePurchase(activePurchase)
                }

                _subscriptionState.value = SubscriptionState.Active
                saveCachedSubscriptionStatus(true)
            } else {
                // Проверяем, является ли пользователь тестировщиком (license tester)
                if (isLicenseTester()) {
                    Log.d(TAG, "🟢 LICENSE TESTER detected - free access granted")
                    _subscriptionState.value = SubscriptionState.TestMode
                    saveCachedSubscriptionStatus(true)
                } else {
                    Log.d(TAG, "⚠️ No active subscription found")
                    _subscriptionState.value = SubscriptionState.Inactive(
                        "Aucun abonnement actif trouvé"
                    )
                    saveCachedSubscriptionStatus(false)
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error checking subscription", e)

            // Fallback на кэш при ошибке
            if (getCachedSubscriptionStatus()) {
                Log.d(TAG, "Using cached status due to error")
                _subscriptionState.value = SubscriptionState.Active
            } else {
                _subscriptionState.value = SubscriptionState.Error(
                    "Erreur: ${e.message}"
                )
            }
        }
    }

    /**
     * Запуск процесса покупки подписки
     */
    suspend fun launchSubscriptionFlow(activity: Activity) = withContext(Dispatchers.Main) {
        val client = billingClient
        if (client == null || !client.isReady) {
            _purchaseFlow.value = PurchaseResult.Error(
                "Service d'abonnement non disponible"
            )
            return@withContext
        }

        try {
            // Получаем детали продукта
            val productList = listOf(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(SUBSCRIPTION_PRODUCT_ID)
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            )

            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build()

            val productDetailsResult = withContext(Dispatchers.IO) {
                client.queryProductDetails(params)
            }

            val productDetails = productDetailsResult.productDetailsList?.firstOrNull()

            if (productDetails == null) {
                Log.e(TAG, "Product details not found")
                _purchaseFlow.value = PurchaseResult.Error(
                    "Abonnement non disponible"
                )
                return@withContext
            }

            // Получаем офферы подписки
            val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken

            if (offerToken == null) {
                Log.e(TAG, "No subscription offers found")
                _purchaseFlow.value = PurchaseResult.Error(
                    "Offres d'abonnement non disponibles"
                )
                return@withContext
            }

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

            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                Log.e(TAG, "Failed to launch billing flow: ${billingResult.debugMessage}")
                _purchaseFlow.value = PurchaseResult.Error(
                    "Erreur lors du lancement de l'achat"
                )
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error launching subscription flow", e)
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
                    if (purchase.products.contains(SUBSCRIPTION_PRODUCT_ID)) {
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

        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                acknowledgePurchase(purchase)
            }

            _subscriptionState.value = SubscriptionState.Active
            _purchaseFlow.value = PurchaseResult.Success
            saveCachedSubscriptionStatus(true)
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
        return (android.os.Build.FINGERPRINT.startsWith("generic")
                || android.os.Build.FINGERPRINT.startsWith("unknown")
                || android.os.Build.MODEL.contains("google_sdk")
                || android.os.Build.MODEL.contains("Emulator")
                || android.os.Build.MODEL.contains("Android SDK built for x86")
                || android.os.Build.MANUFACTURER.contains("Genymotion")
                || (android.os.Build.BRAND.startsWith("generic") && android.os.Build.DEVICE.startsWith("generic"))
                || "google_sdk" == android.os.Build.PRODUCT)
    }

    /**
     * Проверка, является ли пользователь тестировщиком по лицензии
     *
     * ВАЖНО: В Google Play Console добавьте email-адреса тестировщиков
     * в "Настройки → Управление тестированием → Лицензионное тестирование"
     */
    private fun isLicenseTester(): Boolean {
        // Google Play автоматически предоставляет тестовые покупки для license testers
        // Мы можем проверить это через наличие тестовой покупки

        // Дополнительно: можно добавить список email'ов тестировщиков
        // и проверять через GoogleSignIn API, но это требует дополнительной настройки

        return false // По умолчанию false, Google Play сам обработает license testers
    }

    /**
     * Сохранение кэша статуса подписки (для offline доступа)
     */
    private fun saveCachedSubscriptionStatus(isActive: Boolean) {
        try {
            val file = File(context.getExternalFilesDir(null), SUBSCRIPTION_CACHE_FILE)
            val timestamp = System.currentTimeMillis()
            file.writeText("$isActive,$timestamp")
            Log.d(TAG, "Subscription status cached: $isActive")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to cache subscription status", e)
        }
    }

    /**
     * Получение кэшированного статуса подписки
     * Кэш действителен 7 дней
     */
    private fun getCachedSubscriptionStatus(): Boolean {
        try {
            val file = File(context.getExternalFilesDir(null), SUBSCRIPTION_CACHE_FILE)
            if (!file.exists()) return false

            val content = file.readText().split(",")
            if (content.size != 2) return false

            val isActive = content[0].toBoolean()
            val timestamp = content[1].toLong()
            val currentTime = System.currentTimeMillis()
            val daysPassed = (currentTime - timestamp) / (1000 * 60 * 60 * 24)

            // Кэш действителен 7 дней
            return isActive && daysPassed < 7
        } catch (e: Exception) {
            Log.e(TAG, "Failed to read cached subscription status", e)
            return false
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