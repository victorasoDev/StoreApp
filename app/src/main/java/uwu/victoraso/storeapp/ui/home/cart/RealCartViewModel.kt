package uwu.victoraso.storeapp.ui.home.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import uwu.victoraso.storeapp.model.*
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import uwu.victoraso.storeapp.ui.home.feed.FeedUiState
import javax.inject.Inject

@HiltViewModel
class RealCartViewModel
@Inject
constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val inspiredByCartProductsStream: Flow<Result<List<Product>>> = productRepository.getProductsByCategory("Graphic Cards").asResult()
    private val cartProductsStream: Flow<Result<List<Product>>> = productRepository.getProductsByCategory("Graphics Cards").asResult() //TODO

    val uiState: StateFlow<CartScreenUiState> =
        combine(
            inspiredByCartProductsStream,
            cartProductsStream
        ) { inspiredByCartProductsResult, cartProductsResult ->
            val inspiredByCart: InspiredByCartProductsUiState =
                when (inspiredByCartProductsResult) {
                    is Result.Success -> InspiredByCartProductsUiState.Success(
                        ProductCollection(
                            id = 1L,
                            name = "Inspired By Cart",
                            products = inspiredByCartProductsResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> InspiredByCartProductsUiState.Loading
                    is Result.Error -> InspiredByCartProductsUiState.Error
                }
            CartScreenUiState(inspiredByCart)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CartScreenUiState(
                    InspiredByCartProductsUiState.Loading
                )
            )

}

sealed interface InspiredByCartProductsUiState {
    data class Success(val productCollection: ProductCollection) : InspiredByCartProductsUiState
    object Error : InspiredByCartProductsUiState
    object Loading : InspiredByCartProductsUiState
}

data class CartScreenUiState(
    val inspiredByCartProductsUiState: InspiredByCartProductsUiState,
    //TODO: cart
)