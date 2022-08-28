package uwu.victoraso.storeapp.ui.productcreate

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import java.util.*
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

    //TODO pasar el objeto ya construido
    fun addNewProduct(
        name: String,
        price: String,
        tagline: String,
        categories: List<String> = emptyList(),
    ) {
        Log.d(DEBUG_TAG, "currentIndex ${ProductRepository.lastIndex}")
        val product = Product(
            id = ProductRepository.lastIndex,
            name = name,
            price = price.toLong(),
            tagline = tagline,
            categories = categories
        )

        productRepository.addNewProduct(product)
    }
}