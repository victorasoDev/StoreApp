package uwu.victoraso.storeapp.repositories.products

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository
@Inject
constructor(
    private val db: FirebaseFirestore,
    private val productDataSource: ProductDataSource,
) : ProductRepositoryInterface
{
    companion object {
        var lastIndex = 0L
    }

    init {
        saveLastIndex()
    }

    override fun addNewProduct(product: Product): Boolean = productDataSource.addNewProduct(product)

    override fun getMainList(): Flow<List<Product>> = productDataSource.getMainList()

    override fun getProductsByCategory(category: String): Flow<List<Product>> = productDataSource.getProductsByIds(category)

    override fun getProductDetailsById(productId: Long): Flow<Product> = productDataSource.getProductDetailsById(productId)

    override fun getProductsByInputText(inputText: String): Flow<List<Product>> = productDataSource.getProductsByInputText(inputText)

    override fun getProductsByIds(productIds: List<Int>): Flow<List<Product>> = productDataSource.getProductsByIds(productIds)

    /**
     * DEBUG
     * **/
    private fun saveLastIndex() {
        db.collection("products")
            .get()
            .addOnSuccessListener {
                lastIndex = it.size().toLong() + 1
                Log.d(DEBUG_TAG, "lastIndex -> ${lastIndex}")
            }
            .addOnFailureListener {
                Log.d(DEBUG_TAG, "${it}")
            }
    }
    /**
     * DEBUG
     * **/
}

sealed interface ProductRepositoryInterface {
    fun addNewProduct(product: Product): Boolean
    fun getMainList(): Flow<List<Product>>
    fun getProductsByCategory(category: String): Flow<List<Product>>
    fun getProductDetailsById(productId: Long): Flow<Product>
    fun getProductsByInputText(inputText: String): Flow<List<Product>>
    fun getProductsByIds(productIds: List<Int>): Flow<List<Product>>
}