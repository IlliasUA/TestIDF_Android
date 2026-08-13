/*
 * ПЛАТНАЯ ПОДПИСКА ОТКЛЮЧЕНА.
 * ViewModel сохранена в комментарии на случай будущего восстановления функции.
package legOS.testidf.viewmodel

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import legOS.testidf.billing.BillingManager

data class SubscriptionUiState(
    val isActive: Boolean = false,
    val isLoading: Boolean = true,
    val isTestMode: Boolean = false,
    val subscriptionType: String = "none", // "monthly", "annual", "test", "none"
    val errorMessage: String? = null,
    val isPurchasing: Boolean = false,
    val purchaseSuccess: Boolean = false
)

/**
 * ИСПРАВЛЕНО: Добавлен сброс isPurchasing при отмене покупки
 */
class SubscriptionViewModel(application: Application) : AndroidViewModel(application) {

    private val billingManager = BillingManager(application)

    private val _uiState = MutableStateFlow(SubscriptionUiState())
    val uiState: StateFlow<SubscriptionUiState> = _uiState

    companion object {
        private const val TAG = "SubscriptionViewModel"
    }

    init {
        Log.d(TAG, "Initializing SubscriptionViewModel")
        initializeBilling()
    }

    /**
     * Инициализация биллинга
     */
    private fun initializeBilling() {
        viewModelScope.launch {
            try {
                billingManager.initialize()

                // Подписываемся на изменения состояния подписки
                launch {
                    billingManager.subscriptionState.collect { state ->
                        Log.d(TAG, "Subscription state changed: $state")

                        when (state) {
                            is BillingManager.SubscriptionState.Loading -> {
                                _uiState.value = _uiState.value.copy(
                                    isLoading = true
                                )
                            }
                            is BillingManager.SubscriptionState.Active -> {
                                _uiState.value = _uiState.value.copy(
                                    isActive = true,
                                    isLoading = false,
                                    isTestMode = false,
                                    subscriptionType = state.subscriptionType,
                                    errorMessage = null
                                )
                            }
                            is BillingManager.SubscriptionState.Inactive -> {
                                _uiState.value = _uiState.value.copy(
                                    isActive = false,
                                    isLoading = false,
                                    isTestMode = false,
                                    subscriptionType = "none",
                                    errorMessage = state.reason
                                )
                            }
                            is BillingManager.SubscriptionState.Error -> {
                                _uiState.value = _uiState.value.copy(
                                    isActive = false,
                                    isLoading = false,
                                    isTestMode = false,
                                    subscriptionType = "none",
                                    errorMessage = state.message
                                )
                            }
                            is BillingManager.SubscriptionState.TestMode -> {
                                _uiState.value = _uiState.value.copy(
                                    isActive = true,
                                    isLoading = false,
                                    isTestMode = true,
                                    subscriptionType = "test",
                                    errorMessage = null
                                )
                            }
                        }
                    }
                }

                // ИСПРАВЛЕНО: Подписываемся на результаты покупок с правильным сбросом isPurchasing
                launch {
                    billingManager.purchaseFlow.collect { result ->
                        when (result) {
                            is BillingManager.PurchaseResult.Success -> {
                                Log.d(TAG, "Purchase successful!")
                                _uiState.value = _uiState.value.copy(
                                    isPurchasing = false,
                                    purchaseSuccess = true,
                                    isActive = true,
                                    errorMessage = null
                                )
                            }
                            is BillingManager.PurchaseResult.Error -> {
                                Log.e(TAG, "Purchase error: ${result.message}")
                                _uiState.value = _uiState.value.copy(
                                    isPurchasing = false,
                                    errorMessage = result.message
                                )
                            }
                            is BillingManager.PurchaseResult.Cancelled -> {
                                Log.d(TAG, "Purchase cancelled by user")
                                // ИСПРАВЛЕНО: Сбрасываем isPurchasing и очищаем ошибки
                                _uiState.value = _uiState.value.copy(
                                    isPurchasing = false,
                                    errorMessage = null
                                )
                            }
                            null -> {
                                // Начальное состояние - ничего не делаем
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error initializing billing", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erreur d'initialisation: ${e.message}"
                )
            }
        }
    }

    /**
     * Проверка статуса подписки
     */
    fun checkSubscription() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                billingManager.checkSubscriptionStatus()
            } catch (e: Exception) {
                Log.e(TAG, "Error checking subscription", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erreur: ${e.message}"
                )
            }
        }
    }

    /**
     * Запуск процесса покупки
     * @param productId - ID подписки (месячная или годовая)
     */
    fun purchaseSubscription(
        activity: Activity,
        productId: String = BillingManager.SUBSCRIPTION_ANNUAL_PRODUCT_ID
    ) {
        viewModelScope.launch {
            try {
                // ИСПРАВЛЕНО: Сбрасываем предыдущие ошибки при новой попытке покупки
                _uiState.value = _uiState.value.copy(
                    isPurchasing = true,
                    errorMessage = null
                )

                Log.d(TAG, "Launching purchase for: $productId")
                billingManager.launchSubscriptionFlow(activity, productId)

            } catch (e: Exception) {
                Log.e(TAG, "Error launching purchase", e)
                _uiState.value = _uiState.value.copy(
                    isPurchasing = false,
                    errorMessage = "Erreur: ${e.message}"
                )
            }
        }
    }

    /**
     * Очистка сообщения об ошибке
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    /**
     * Сброс флага успешной покупки
     */
    fun clearPurchaseSuccess() {
        _uiState.value = _uiState.value.copy(purchaseSuccess = false)
    }

    override fun onCleared() {
        super.onCleared()
        billingManager.destroy()
        Log.d(TAG, "ViewModel cleared")
    }
}
*/
