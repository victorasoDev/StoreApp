package uwu.victoraso.storeapp.ui.productcollection

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
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
    Log.d(DEBUG_TAG,"processorProducts in ViewModel -> ${productListByCategory.asResult()}")
    return productListByCategory
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> {
                    Log.d(DEBUG_TAG," --- Success")
                    ProductListUiState.Success(
                        productListByCategory.first()
                    )
                }
                is Result.Loading -> {
                    Log.d(DEBUG_TAG," --- Loading")
                    ProductListUiState.Loading
                }
                is Result.Error -> {
                    Log.d(DEBUG_TAG," --- Error")
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