package uwu.victoraso.storeapp.repositories

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import uwu.victoraso.storeapp.db.StoreAppModule
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

//            val docRef = StoreAppModule.provideFirestoreInstance().collection("products").document("p-001")
//            docRef.get()
//                .addOnSuccessListener { document ->
//                    Log.d("debugprueba", document.data.toString())
//                }
//                .addOnFailureListener {
//                    Log.d("debugprueba", it.toString())
//                }
//                .await()

            val docRefAllElements = StoreAppModule.provideFirestoreInstance().collection("products")
                .whereEqualTo("id", "p-003")
                .get()
                .addOnSuccessListener {
                    for (doc in it) {
                        //Aquí habría que parsear a objeto
                        Log.d("debugprueba", "${doc.data}")
                    }
                }
                .addOnFailureListener {
                    Log.d("debugprueba", it.toString())
                }
                .await()


            val productList = productList.get().await().map { product ->
                product.toObject(Product::class.java)
            }

            emit(Result.Success(data = productList))
        } catch (e: Exception) {
            emit(Result.Error(message = e.localizedMessage ?: "Unknown error"))
        }
    }

}