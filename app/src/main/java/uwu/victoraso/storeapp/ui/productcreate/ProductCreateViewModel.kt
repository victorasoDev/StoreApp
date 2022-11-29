package uwu.victoraso.storeapp.ui.productcreate

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uwu.victoraso.storeapp.model.products
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import javax.inject.Inject

@HiltViewModel
class ProductCreateViewModel
@Inject
constructor(
    private val productRepository: ProductRepository
): ViewModel()
{
    private val _state: MutableState<ProductCreateState> = mutableStateOf(ProductCreateState())
    val state: State<ProductCreateState> get() = _state

    fun addNewProduct() {
        Log.d(DEBUG_TAG, "currentIndex ${ProductRepository.lastIndex}")
        for (p in products) {
            productRepository.addNewProduct(p)
        }
    }
}