package uwu.victoraso.storeapp.repositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import uwu.victoraso.storeapp.model.Product
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository
@Inject
constructor(
    private val productList: CollectionReference
){

    fun addNewProduct(product: Product) {
        try {
            productList.document(product.id.toString()).set(product)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getProductList() : Flow<Result<List<Product>>> = flow {
        try {
            emit(Result.Loading())

            val productList = productList.get().await().map { product ->
                product.toObject(Product::class.java)
            }

            emit(Result.Success(data = productList))
        } catch (e: Exception) {
            emit(Result.Error(message = e.localizedMessage ?: "Unknown error"))
        }
    }

}