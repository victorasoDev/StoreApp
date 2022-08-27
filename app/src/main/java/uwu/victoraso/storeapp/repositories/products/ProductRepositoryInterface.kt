package uwu.victoraso.storeapp.repositories.products

import kotlinx.coroutines.flow.Flow
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.repositories.Result

interface ProductRepositoryInterface {

    fun addNewProduct(product: Product)
    fun getProductList(): Flow<Result<List<Product>>>
}