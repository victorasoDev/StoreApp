package uwu.victoraso.storeapp.ui.home.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.OrderLine
import uwu.victoraso.storeapp.model.ProductRepo
import uwu.victoraso.storeapp.model.SnackbarManager

class CartViewModel(
    private val snackbarManager: SnackbarManager,
    productRepository: ProductRepo
) : ViewModel() {

    private val _orderLines: MutableStateFlow<List<OrderLine>> =
        MutableStateFlow(productRepository.getCart())
    val orderLines: StateFlow<List<OrderLine>> get() = _orderLines //TODO: informarse sobre los flows { https://developer.android.com/kotlin/flow/stateflow-and-sharedflow?hl=es-419 }

    // Logic to show errors every few requests
    private var requestCount = 0
    private fun shouldRandomlyFail(): Boolean = ++requestCount % 5 == 0 //TODO

    fun increaseProductCount(productId: Long) {
        if (!shouldRandomlyFail()) {
            val currentCount = _orderLines.value.first { it.product.id == productId }.count
            updateProductCount(productId, currentCount + 1)
        } else {
            snackbarManager.showMessage(R.string.cart_increase_error)
        }
    }

    fun decreaseProductCount(productId: Long) {
        if (!shouldRandomlyFail()) {
            val currentCount = _orderLines.value.first { it.product.id == productId }.count
            if (currentCount == 1) {
                removeProduct(productId)
            } else {
                updateProductCount(productId, currentCount - 1)
            }
        } else {
            snackbarManager.showMessage(R.string.cart_decrease_error)
        }
    }

    fun removeProduct(productId: Long) {
        //super molón -> arrayList de aquellos items que no tengan el $productId dado
        _orderLines.value = _orderLines.value.filter { it.product.id != productId }
    }

    private fun updateProductCount(productId: Long, count: Int) {
        _orderLines.value = orderLines.value.map {
            if (it.product.id == productId) {
                it.copy(count = count)
            } else {
                it
            }
        }
    }

    /**
     * Factory for CartViewModel that takes SnackbarManager as a dependency
     */
    companion object {
        fun provideFactory(
            snackbarManager: SnackbarManager = SnackbarManager,
            productRepository: ProductRepo = ProductRepo
        ) : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CartViewModel(snackbarManager, productRepository) as T
            }
        }
    }
}