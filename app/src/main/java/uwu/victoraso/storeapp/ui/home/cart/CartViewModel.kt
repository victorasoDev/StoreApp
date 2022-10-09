package uwu.victoraso.storeapp.ui.home.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uwu.victoraso.storeapp.model.*
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.repositories.cart.CartRepository
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import javax.inject.Inject

@HiltViewModel
class CartViewModel
@Inject
constructor(
    productRepository: ProductRepository,
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val inspiredByCartProductsStream: Flow<Result<List<Product>>> = productRepository.getProductsByCategory("Graphic Cards").asResult()
//    private val cartStream: Flow<Result<Cart>> = cartRepository.getCartByIdStream("1").asResult()
    private val cartsStream: Flow<Result<List<Cart>>> = cartRepository.getCartsStream().asResult()

    val uiState: StateFlow<CartScreenUiState> =
        combine(
            inspiredByCartProductsStream,
            cartsStream
        ) { inspiredByCartProductsResult, cartsResult ->
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

            val cart: CartsProductsUiState =
                when (cartsResult) {
                    is Result.Success -> CartsProductsUiState.Success(cartsResult.data)
                    is Result.Loading -> CartsProductsUiState.Loading
                    is Result.Error -> CartsProductsUiState.Error
                }
            CartScreenUiState(inspiredByCart, cart)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CartScreenUiState(
                    InspiredByCartProductsUiState.Loading,
                    CartsProductsUiState.Loading
                )
            )

    fun removeProduct(productId: Long, cartId: Long) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(productId.toString(), cartId.toString())
        }
    }
}

sealed interface InspiredByCartProductsUiState {
    data class Success(val productCollection: ProductCollection) : InspiredByCartProductsUiState
    object Error : InspiredByCartProductsUiState
    object Loading : InspiredByCartProductsUiState
}

sealed interface CartsProductsUiState {
    data class Success(val carts: List<Cart>) : CartsProductsUiState
    object Error : CartsProductsUiState
    object Loading : CartsProductsUiState
}

data class CartScreenUiState(
    val inspiredByCartProductsUiState: InspiredByCartProductsUiState,
    val cartProductsUiState: CartsProductsUiState,
)