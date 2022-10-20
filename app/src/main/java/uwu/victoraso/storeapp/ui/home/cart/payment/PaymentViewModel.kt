package uwu.victoraso.storeapp.ui.home.cart.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import uwu.victoraso.storeapp.model.UserProfile
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.repositories.userpreferences.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

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
}

sealed interface PaymentDataUiState {
    data class Success(val userProfile: UserProfile) : PaymentDataUiState
    object Error : PaymentDataUiState
    object Loading : PaymentDataUiState
}