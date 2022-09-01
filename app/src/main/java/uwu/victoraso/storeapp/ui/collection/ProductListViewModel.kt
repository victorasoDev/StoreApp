package uwu.victoraso.storeapp.ui.collection

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel
@Inject
constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _state: MutableState<ProductListState> = mutableStateOf(ProductListState())
    val state: State<ProductListState> get() = _state

    fun addProductToCart(id: Long) {

    }

    fun removeProductFromCart(id: Long) {

    }

}