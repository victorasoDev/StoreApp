package uwu.victoraso.storeapp.ui.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uwu.victoraso.storeapp.MainDestinations
import uwu.victoraso.storeapp.model.*
import uwu.victoraso.storeapp.model.service.AccountService
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.repositories.cart.CartRepository
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import uwu.victoraso.storeapp.repositories.wishlist.WishlistRepository
import uwu.victoraso.storeapp.room.model.CartEntity
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel
@Inject
constructor(
    productRepository: ProductRepository,
    private val wishlistRepository: WishlistRepository,
    private val cartRepository: CartRepository,
    private val accountService: AccountService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val productId: Long = checkNotNull(savedStateHandle[MainDestinations.PRODUCT_ID_KEY])
    private val category: String = checkNotNull(savedStateHandle[MainDestinations.CATEGORY_ID_KEY])

    private val productDetailStream: Flow<Result<Product>> = productRepository.getProductDetailsById(productId = productId).asResult()
    private val relatedProductsStream: Flow<Result<List<Product>>> = productRepository.getProductsByCategory(category = category).asResult()
    private val isWishlistedStream: Flow<Result<Boolean>> =
        wishlistRepository.isWishlisted(productId = productId, userId = accountService.getUserId()).asResult()
    private val cartsStream: Flow<Result<List<Cart>>> = cartRepository.getCartsStream().asResult()

    val uiState: StateFlow<ProductDetailScreenUiState> =
        combine(
            productDetailStream,
            relatedProductsStream,
            isWishlistedStream,
            cartsStream
        ) { productDetailResult, relatedProductsResult, isWishlistedResult, cartsResult ->
            /**
             * Get product details
             * */
            val product: ProductDetailUiState =
                when (productDetailResult) {
                    is Result.Success -> {
                        val isWishlisted = if (isWishlistedResult is Result.Success) isWishlistedResult.data else false
                        ProductDetailUiState.Success(
                            Product(
                                id = productDetailResult.data.id,
                                name = productDetailResult.data.name,
                                imageUrl = productDetailResult.data.imageUrl,
                                iconUrl = productDetailResult.data.iconUrl,
                                price = productDetailResult.data.price,
                                tagline = productDetailResult.data.tagline,
                                category = productDetailResult.data.category,
                                isWishlist = isWishlisted
                            )
                        )
                    }
                    is Result.Loading -> ProductDetailUiState.Loading
                    is Result.Error -> ProductDetailUiState.Error
                }

            /**
             * Get related products of current selected
             * */
            val relatedProducts: RelatedProductsUiState =
                when (relatedProductsResult) {
                    is Result.Success -> RelatedProductsUiState.Success(
                        ProductCollection(
                            id = 1L,
                            name = relatedProductsResult.data.first().category, //TODO: mucho first por aqui
                            products = relatedProductsResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> RelatedProductsUiState.Loading
                    is Result.Error -> RelatedProductsUiState.Error
                }

            /**
             * Get user carts
             * */
            val userCarts: CartsUiState =
                when (cartsResult) {
                    is Result.Success -> CartsUiState.Success(cartsResult.data)
                    is Result.Loading -> CartsUiState.Loading
                    is Result.Error -> CartsUiState.Error
                }
            ProductDetailScreenUiState(product, relatedProducts, userCarts)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ProductDetailScreenUiState(
                    product = ProductDetailUiState.Loading,
                    relatedProducts = RelatedProductsUiState.Loading,
                    carts = CartsUiState.Loading
                )
            )

    fun wishlistItemToggle(productId: Long, wishlist: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (accountService.hasUser()) {
                wishlistRepository.wishlistToggle(productId, accountService.getUserId(), wishlist)
            }
        }
    }

    fun addToCart(cartProduct: CartProduct) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.insertCartProduct(cartProduct.asEntity())
        }
    }

    fun addCart(cart: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.insertCart(CartEntity(name = cart.name)) //TODO ?
        }
    }
}

sealed interface ProductDetailUiState {
    data class Success(val product: Product) : ProductDetailUiState
    object Error : ProductDetailUiState
    object Loading : ProductDetailUiState
}

sealed interface RelatedProductsUiState {
    data class Success(val productCollection: ProductCollection) : RelatedProductsUiState
    object Error : RelatedProductsUiState
    object Loading : RelatedProductsUiState
}

sealed interface CartsUiState {
    data class Success(val carts: List<Cart>) : CartsUiState
    object Error : CartsUiState
    object Loading : CartsUiState
}

data class ProductDetailScreenUiState(
    val product: ProductDetailUiState,
    val relatedProducts: RelatedProductsUiState,
    val carts: CartsUiState
)