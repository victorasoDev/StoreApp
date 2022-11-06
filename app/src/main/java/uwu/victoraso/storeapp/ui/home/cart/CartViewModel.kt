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
import uwu.victoraso.storeapp.repositories.userpreferences.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class CartViewModel
@Inject
constructor(
    productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val inspiredByCartProductsStream: Flow<Result<List<Product>>> = productRepository.getProductsByCategory("Graphic Cards").asResult()
//    private val cartStream: Flow<Result<Cart>> = cartRepository.getCartByIdStream("1").asResult()
    private val cartsStream: Flow<Result<List<Cart>>> = cartRepository.getCartsStream().asResult()
    private val selectedCartStream: Flow<Result<Int>> = userPreferencesRepository.selectedCartIndex.asResult()

    val uiState: StateFlow<CartScreenUiState> =
        combine(
            inspiredByCartProductsStream,
            cartsStream,
            selectedCartStream
        ) { inspiredByCartProductsResult, cartsResult, selectedCartResult ->
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
            val selectedCart: SelectedCartUiState =
                when (selectedCartResult) {
                    is Result.Success -> SelectedCartUiState.Success(selectedCartResult.data)
                    else -> SelectedCartUiState.None
                }
            CartScreenUiState(inspiredByCart, cart, selectedCart)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CartScreenUiState(
                    InspiredByCartProductsUiState.Loading,
                    CartsProductsUiState.Loading,
                    SelectedCartUiState.None
                )
            )

    fun removeProduct(productId: Long, cartId: Long) {
        viewModelScope.launch {
            cartRepository.deleteCartProduct(productId.toString(), cartId.toString())
        }
    }

    fun changeCartName(cart: Cart) {
        viewModelScope.launch {
            cartRepository.updateCart(cart.asEntity())
        }
    }

    fun setSelectedCartIndex(selectedCartIndex: Int) {
        viewModelScope.launch {
            userPreferencesRepository.setSelectedCartIndex(selectedCartIndex)
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

sealed interface SelectedCartUiState {
    data class Success(val selectedCartIndex: Int) : SelectedCartUiState
    object None : SelectedCartUiState
}

data class CartScreenUiState(
    val inspiredByCartProductsUiState: InspiredByCartProductsUiState,
    val cartProductsUiState: CartsProductsUiState,
    val selectedCartUiState: SelectedCartUiState,
)