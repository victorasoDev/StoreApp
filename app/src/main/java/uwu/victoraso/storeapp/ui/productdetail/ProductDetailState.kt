package uwu.victoraso.storeapp.ui.productdetail

import uwu.victoraso.storeapp.model.Product

data class ProductDetailState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val error: String? = ""
)