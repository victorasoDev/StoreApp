package uwu.victoraso.storeapp.ui.home.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject
constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {

    fun search(inputText: String): Flow<List<Product>> = productRepository.getProductsByInputText(inputText)
}