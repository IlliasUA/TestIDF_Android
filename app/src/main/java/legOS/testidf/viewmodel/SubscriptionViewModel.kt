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
    val errorMessage: String? = null,
    val isPurchasing: Boolean = false,
    val purchaseSuccess: Boolean = false
)

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
                                    errorMessage = null
                                )
                            }
                            is BillingManager.SubscriptionState.Inactive -> {
                                _uiState.value = _uiState.value.copy(
                                    isActive = false,
                                    isLoading = false,
                                    isTestMode = false,
                                    errorMessage = state.reason
                                )
                            }
                            is BillingManager.SubscriptionState.Error -> {
                                _uiState.value = _uiState.value.copy(
                                    isActive = false,
                                    isLoading = false,
                                    isTestMode = false,
                                    errorMessage = state.message
                                )
                            }
                            is BillingManager.SubscriptionState.TestMode -> {
                                _uiState.value = _uiState.value.copy(
                                    isActive = true,
                                    isLoading = false,
                                    isTestMode = true,
                                    errorMessage = null
                                )
                            }
                        }
                    }
                }

                // Подписываемся на результаты покупок
                launch {
                    billingManager.purchaseFlow.collect { result ->
                        when (result) {
                            is BillingManager.PurchaseResult.Success -> {
                                Log.d(TAG, "Purchase successful!")
                                _uiState.value = _uiState.value.copy(
                                    isPurchasing = false,
                                    purchaseSuccess = true,
                                    isActive = true
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
                                _uiState.value = _uiState.value.copy(
                                    isPurchasing = false
                                )
                            }
                            null -> {
                                // Начальное состояние
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
     */
    fun purchaseSubscription(activity: Activity) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isPurchasing = true,
                    errorMessage = null
                )

                billingManager.launchSubscriptionFlow(activity)

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