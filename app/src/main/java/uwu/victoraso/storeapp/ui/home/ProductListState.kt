package uwu.victoraso.storeapp.ui.home

import uwu.victoraso.storeapp.model.Product

data class ProductListState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = ""
)