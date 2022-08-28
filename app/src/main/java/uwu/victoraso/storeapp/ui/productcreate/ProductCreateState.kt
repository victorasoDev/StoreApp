package uwu.victoraso.storeapp.ui.productcreate

import uwu.victoraso.storeapp.model.Product

data class ProductCreateState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val error: String = ""
)