package uwu.victoraso.storeapp.ui.home.profile.purchasehistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import uwu.victoraso.storeapp.model.Purchase
import uwu.victoraso.storeapp.model.service.AccountService
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.repositories.purchases.PurchasesRepository
import javax.inject.Inject

@HiltViewModel
class PurchaseHistoryViewModel
@Inject constructor(
    purchasesRepository: PurchasesRepository,
    accountService: AccountService,
) : ViewModel() {

    val purchaseHistoryUiState: StateFlow<PurchaseHistoryUiState> = purchaseHistoryStateStream(
        userId = accountService.getUserId(),
        purchasesRepository = purchasesRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PurchaseHistoryUiState.Loading
        )
}

private fun purchaseHistoryStateStream(
    userId: String,
    purchasesRepository: PurchasesRepository
): Flow<PurchaseHistoryUiState> {
    val purchaseHistoryList: Flow<List<Purchase>> = purchasesRepository.getUserPurchases(userId)

    return purchaseHistoryList
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> {
                    PurchaseHistoryUiState.Success(
                        purchaseHistoryList.first()
                    )
                }
                is Result.Loading -> {
                    PurchaseHistoryUiState.Loading
                }
                is Result.Error -> {
                    PurchaseHistoryUiState.Error
                }
            }
        }
}

sealed interface PurchaseHistoryUiState {
    data class Success(val purchases: List<Purchase>) : PurchaseHistoryUiState
    object Error : PurchaseHistoryUiState
    object Loading : PurchaseHistoryUiState
}
