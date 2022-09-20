package uwu.victoraso.storeapp.ui.productdetail

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import uwu.victoraso.storeapp.MainDestinations
import uwu.victoraso.storeapp.model.CollectionType
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.ProductCollection
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import uwu.victoraso.storeapp.ui.home.feed.FeedUiState
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel
@Inject
constructor(
    productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: Long = checkNotNull(savedStateHandle[MainDestinations.PRODUCT_ID_KEY])
    private val category: String = checkNotNull(savedStateHandle[MainDestinations.CATEGORY_ID_KEY])

    private val productDetailStream: Flow<Result<Product>> = productRepository.getProductDetailsById(productId = productId).asResult()
    private val relatedProductsStream: Flow<Result<List<Product>>> = productRepository.getProductsByCategory(category = category).asResult()

    val uiState: StateFlow<ProductDetailScreenUiState> =
        combine(
            productDetailStream,
            relatedProductsStream
        ) { productDetailResult, relatedProductsResult ->
            /**
             * Get product details
             * */
            val product: ProductDetailUiState =
                when (productDetailResult) {
                    is Result.Success -> ProductDetailUiState.Success(
                        Product(
                            id = productDetailResult.data.id,
                            name = productDetailResult.data.name,
                            imageUrl = productDetailResult.data.imageUrl,
                            price = productDetailResult.data.price,
                            tagline = productDetailResult.data.tagline,
                            categories = productDetailResult.data.categories,
                        )
                    )
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
                            name = relatedProductsResult.data.first().categories.first(),
                            products = relatedProductsResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> RelatedProductsUiState.Loading
                    is Result.Error -> RelatedProductsUiState.Error
                }
            ProductDetailScreenUiState(product, relatedProducts)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ProductDetailScreenUiState(
                    product = ProductDetailUiState.Loading,
                    relatedProducts = RelatedProductsUiState.Loading
                )
            )
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

data class ProductDetailScreenUiState(
    val product: ProductDetailUiState,
    val relatedProducts: RelatedProductsUiState
)