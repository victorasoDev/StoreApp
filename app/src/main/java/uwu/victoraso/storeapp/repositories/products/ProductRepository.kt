package uwu.victoraso.storeapp.repositories.products

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.products.ProductRepositoryInterface
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository
@Inject
constructor(
    private val db: FirebaseFirestore
): ProductRepositoryInterface {

    override fun addNewProduct(product: Product) {
        try {
            db.document(product.id.toString()).set(product)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getProductList() : Flow<Result<List<Product>>> = flow {
        try {
            emit(Result.Loading())

//            val docRef = StoreAppModule.provideFirestoreInstance().collection("products").document("p-001")
//            docRef.get()
//                .addOnSuccessListener { document ->
//                    Log.d("debugprueba", document.data.toString())
//                }
//                .addOnFailureListener {
//                    Log.d("debugprueba", it.toString())
//                }
//                .await()

            val collection = db.collection("products")
                .whereEqualTo("id", 2)
                .get()
                .addOnSuccessListener {
                    for (doc in it) {
                        Log.d(DEBUG_TAG, "${doc.data}")
                    }
                }
                .addOnFailureListener {
                    Log.d(DEBUG_TAG, "${it}")
                }
                .await()

            val docRefAllElements = db.collection("products")
                .whereEqualTo("id", "p-001")
                .get()
                .addOnSuccessListener {
                    for (doc in it) {
                        //Aquí habría que parsear a objeto
                        Log.d(DEBUG_TAG, "${doc.data}")
                    }
                }
                .addOnFailureListener {
                    Log.d(DEBUG_TAG, it.toString())
                }
                .await()

//            val productList = productList.get().await().map { product ->
//                product.toObject(Product::class.java)
//            }

            emit(Result.Success(data = emptyList()))
        } catch (e: Exception) {
            emit(Result.Error(message = e.localizedMessage ?: "Unknown error"))
        }
    }

}