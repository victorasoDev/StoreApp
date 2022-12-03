package uwu.victoraso.storeapp.ui.home.profile.purchasehistory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.Purchase
import uwu.victoraso.storeapp.model.service.AccountService
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import uwu.victoraso.storeapp.repositories.purchases.PurchasesRepository
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import javax.inject.Inject

@HiltViewModel
class PurchaseHistoryViewModel
@Inject constructor(
    private val purchasesRepository: PurchasesRepository,
    private val accountService: AccountService,
    private val productRepository: ProductRepository,
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

    fun getPurchaseProductsStateFlow(productsIds: List<Long>) : StateFlow<PurchaseProductsUiState> = getPurchaseProducts(productsIds)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PurchaseProductsUiState.Loading
        )

    fun getPurchaseProducts(productsIds: List<Long>): Flow<PurchaseProductsUiState> {
        val productList: Flow<List<Product>> = productRepository.getProductsByIds(productsIds)

        return productList
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Success -> {
                        Log.d(DEBUG_TAG," --- Success")
                        PurchaseProductsUiState.Success(
                            productList.first()
                        )
                    }
                    is Result.Loading -> {
                        Log.d(DEBUG_TAG," --- Loading")
                        PurchaseProductsUiState.Loading
                    }
                    is Result.Error -> {
                        Log.d(DEBUG_TAG," --- Error")
                        PurchaseProductsUiState.Error
                    }
                }
            }
    }

}

private fun purchaseHistoryStateStream(
    userId: String,
    purchasesRepository: PurchasesRepository
) : Flow<PurchaseHistoryUiState> {
    val purchaseHistoryList: Flow<List<Purchase>> = purchasesRepository.getUserPurchases(userId)

    return purchaseHistoryList
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> {
                    Log.d(DEBUG_TAG," --- Success")
                    PurchaseHistoryUiState.Success(
                        purchaseHistoryList.first()
                    )
                }
                is Result.Loading -> {
                    Log.d(DEBUG_TAG," --- Loading")
                    PurchaseHistoryUiState.Loading
                }
                is Result.Error -> {
                    Log.d(DEBUG_TAG," --- Error")
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

sealed interface PurchaseProductsUiState {
    data class Success(val purchaseProducts: List<Product>) : PurchaseProductsUiState
    object Error : PurchaseProductsUiState
    object Loading : PurchaseProductsUiState
}
