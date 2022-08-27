package uwu.victoraso.storeapp.ui.productdetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel
@Inject
constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _state: MutableState<ProductDetailState> = mutableStateOf(ProductDetailState())
    val state: State<ProductDetailState> get() = _state

    fun addNewProduct(name: String, price: Long) {
        val product = Product(
            id = 10L,
            name = name,
            tagline = "A tag line",
            imageUrl = "https://source.unsplash.com/pGM4sjt_BdQ",
            price = price
        )

        productRepository.addNewProduct(product)
    }

}