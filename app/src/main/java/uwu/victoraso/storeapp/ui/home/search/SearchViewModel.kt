package uwu.victoraso.storeapp.ui.home.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject
constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {

    suspend fun search(inputText: String): Flow<List<Product>> {
        delay(200)
        return productRepository.getProductsByInputText(inputText)
    }
}