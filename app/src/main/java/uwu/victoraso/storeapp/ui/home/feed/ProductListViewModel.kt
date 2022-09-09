package uwu.victoraso.storeapp.ui.home.feed

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import uwu.victoraso.storeapp.ds.asResult
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel
@Inject
constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _state: MutableState<ProductListState> = mutableStateOf(ProductListState())
    val state: State<ProductListState> = _state

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    var productListUiState: StateFlow<ProductListUiState> = productListStateStream(
        productRepository = productRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProductListUiState.Loading
        )

    init {
        getProductList()
        Log.d(DEBUG_TAG, state.value.products.toString())
    }

    fun getProductList() {
        productRepository.getProductList().onEach { result ->
            when(result) {
                is Result.Error -> {
                    _state.value = ProductListState(error = result.message ?: "Unknown error")
                }
                is Result.Loading -> {
                    _state.value = ProductListState(isLoading = true)
                }
                is Result.Success -> {
                    _state.value = ProductListState(products = result.data ?: emptyList())
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addProductToCart(id: Long) {

    }

    fun removeProductFromCart(id: Long) {

    }
}

private fun productListStateStream(
    productRepository: ProductRepository,
): Flow<ProductListUiState> {
    val productList: Flow<List<Product>> = productRepository.getMainList()
    Log.d(DEBUG_TAG,"productList in ViewModel -> ${productList.asResult()}")

    return productList
        .asResult()
        .map { result ->
            when (result) {
                is uwu.victoraso.storeapp.ds.Result.Success -> {
                    Log.d(DEBUG_TAG," --- Success")
                    ProductListUiState.Success(
                        productList.first()
                    )
                }
                is uwu.victoraso.storeapp.ds.Result.Loading -> {
                    Log.d(DEBUG_TAG," --- Loading")
                    ProductListUiState.Loading
                }
                is uwu.victoraso.storeapp.ds.Result.Error -> {
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