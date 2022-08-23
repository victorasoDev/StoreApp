package uwu.victoraso.storeapp.ui.home.feed

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uwu.victoraso.storeapp.repositories.ProductRepository
import uwu.victoraso.storeapp.repositories.Result
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel
@Inject
constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _state: MutableState<ProductListState> = mutableStateOf(ProductListState())
    val state: State<ProductListState> = _state

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        getProductList()
        Log.d("debugprueba", state.value.products.toString())
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


}