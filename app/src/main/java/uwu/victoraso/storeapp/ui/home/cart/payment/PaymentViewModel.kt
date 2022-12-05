package uwu.victoraso.storeapp.ui.home.cart.payment

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uwu.victoraso.storeapp.model.CardDetails
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.Purchase
import uwu.victoraso.storeapp.model.UserProfile
import uwu.victoraso.storeapp.model.service.AccountService
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.repositories.cart.CartRepository
import uwu.victoraso.storeapp.repositories.purchases.PurchasesRepository
import uwu.victoraso.storeapp.repositories.userpreferences.UserPreferencesRepository
import uwu.victoraso.storeapp.ui.utils.formatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val cartRepository: CartRepository,
    private val purchasesRepository: PurchasesRepository,
    private val accountService: AccountService
    ) : ViewModel() {

    /** UiState when paymentButton is clicked **/
    private var _isPaymentLoading = mutableStateOf(false)
    val isPaymentLoading get() = _isPaymentLoading

    private var _isPaymentCompleted = mutableStateOf(false)
    val isPaymentCompleted get() = _isPaymentCompleted

    val paymentUiState: StateFlow<PaymentDataUiState> = paymentUserDataUiStateStream(
        userDataRepository = userPreferencesRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PaymentDataUiState.Loading
        )

    private fun paymentUserDataUiStateStream(
        userDataRepository: UserPreferencesRepository
    ): Flow<PaymentDataUiState> {
        val userProfile: Flow<UserProfile> = userDataRepository.getUserProfile

        return userProfile
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Success -> {
                        PaymentDataUiState.Success(
                            userProfile.first()
                        )
                    }
                    is Result.Loading -> {
                        PaymentDataUiState.Loading
                    }
                    is Result.Error -> {
                        PaymentDataUiState.Error
                    }
                }
            }
    }

    fun getUserDataAsList(userProfile: UserProfile) = listOf(
        userProfile.name, userProfile.email, userProfile.adress, userProfile.phone
    )

    fun removeProduct(productId: Long, cartId: Long) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(productId.toString(), cartId.toString())
        }
    }

    fun makePurchase(price: Long, cardDetails: CardDetails, products: List<Product>): Boolean {
        _isPaymentLoading.startLoading()

        if (cardDetails.cardNumber.length != 8) return purchaseNotCompleted()
        if (!cardDetails.cardExpireDate.matches(Regex("(?:0[1-9]|1[0-2])/[0-9]{2}"))) return purchaseNotCompleted()
        if (cardDetails.cardCVC.length != 3) return purchaseNotCompleted()

        viewModelScope.launch {
            val userData = userPreferencesRepository.getUserProfile.first()
            val makePurchase = purchasesRepository.makePurchase(
                purchase = Purchase(
                    id = UUID.randomUUID().toString(),
                    userID = accountService.getUserId(),
                    userName = userData.name,
                    userAdress = userData.adress,
                    userPhone = userData.phone,
                    cardDetails = cardDetails,
                    price = price,
                    date = formatter.format(Date()),
                    products = products
                )
            )
            delay(2_000)
            if (makePurchase) _isPaymentCompleted.value = true
            _isPaymentLoading.stopLoading()
        }
        return _isPaymentCompleted.value
    }

    private fun purchaseNotCompleted(): Boolean {
        _isPaymentLoading.stopLoading()
        return false
    }

    private fun MutableState<Boolean>.startLoading() { this.value = true }
    private fun MutableState<Boolean>.stopLoading() { this.value = false }
}

sealed interface PaymentDataUiState {
    data class Success(val userProfile: UserProfile) : PaymentDataUiState
    object Error : PaymentDataUiState
    object Loading : PaymentDataUiState
}