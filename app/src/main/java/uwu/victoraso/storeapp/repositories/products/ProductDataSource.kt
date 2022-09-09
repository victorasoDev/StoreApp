package uwu.victoraso.storeapp.repositories.products

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {
    fun getMainList(): Flow<List<Product>> = flow {
        val productList = ArrayList<Product>()
        db.collection("products")
//            .whereEqualTo("id", 1)
            .get()
            .addOnSuccessListener {
                for (doc in it) {
                    productList.add(doc.toObject(Product::class.java))
                    Log.d(DEBUG_TAG, "ProductDataSource -> ${doc.data}")
                }
            }
            .addOnFailureListener {
                Log.d(DEBUG_TAG, "ProductDataSource -> ${it}")
            }
            .await()

        Log.d(DEBUG_TAG, "ProductDataSource SIZE Arraylist-> ${productList.size}")
        emit(productList)
    }

    fun addNewProduct(product: Product): Boolean {
        try {
            db.collection("products").document(product.id.toString()).set(product)
            ProductRepository.lastIndex += 1
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}
