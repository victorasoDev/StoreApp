package uwu.victoraso.storeapp.ui.productcollection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel
@Inject
constructor(
    productRepository: ProductRepository,
) : ViewModel() {

    var productListUiState: StateFlow<ProductListUiState> = productListStateStream(
        productRepository = productRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProductListUiState.Loading
        )

    fun addProductToCart(id: Long) {

    }

    fun removeProductFromCart(id: Long) {

    }

    companion object { //TODO: quitar cuando sepa como pasarle el parámetro
        var categorySelected = ""
    }
}

private fun productListStateStream(
    productRepository: ProductRepository,
): Flow<ProductListUiState> {
    val productListByCategory: Flow<List<Product>> = productRepository.getProductsByCategory(ProductListViewModel.categorySelected)
    return productListByCategory
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> {
                    ProductListUiState.Success(
                        productListByCategory.first()
                    )
                }
                is Result.Loading -> {
                    ProductListUiState.Loading
                }
                is Result.Error -> {
                    ProductListUiState.Error
                }
            }
        }
}

sealed interface ProductListUiState {
    data class Success(val mainList: List<Product>) : ProductListUiState
    object Error : ProductListUiState
    object Loading : ProductListUiState
}