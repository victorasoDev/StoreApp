package uwu.victoraso.storeapp.repositories.products

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import uwu.victoraso.storeapp.model.Product
import javax.inject.Inject

class ProductDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend fun getMainList() : Flow<List<Product>>? = null
}