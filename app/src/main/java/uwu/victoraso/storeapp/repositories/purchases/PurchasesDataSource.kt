package uwu.victoraso.storeapp.repositories.purchases

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import uwu.victoraso.storeapp.model.Purchase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PurchasesDataSource
@Inject constructor(
    private val db: FirebaseFirestore
) {

    fun makePurchase(purchase: Purchase): Boolean {
        try {
            db.collection("purchases").document(purchase.id).set(purchase)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getUserPurchases(userId: String): Flow<List<Purchase>> = flow {
        val purchases = ArrayList<Purchase>()
        db.collection("purchases")
            .whereEqualTo("userID", userId)
            .get()
            .addOnSuccessListener {
                for (doc in it) {
                    purchases.add(doc.toObject(Purchase::class.java))
                }
            }
            .addOnFailureListener {
            }
            .await()

        emit(purchases)
    }
}