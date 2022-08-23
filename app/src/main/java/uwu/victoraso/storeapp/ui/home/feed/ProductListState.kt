package uwu.victoraso.storeapp.ui.home.feed

import uwu.victoraso.storeapp.model.Product

data class ProductListState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = ""
)