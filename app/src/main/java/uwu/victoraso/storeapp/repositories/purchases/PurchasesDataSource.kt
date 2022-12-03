package uwu.victoraso.storeapp.repositories.purchases

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import uwu.victoraso.storeapp.model.Purchase
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
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
        Log.d(DEBUG_TAG, "PurchaseDataSource -> getUserPurchases")
        val purchases = ArrayList<Purchase>()
        db.collection("purchases")
            .whereEqualTo("userID", userId)
            .get()
            .addOnSuccessListener {
                for (doc in it) {
                    Log.d(DEBUG_TAG, "PurchaseDataSource -> $doc")
                    purchases.add(doc.toObject(Purchase::class.java))
                }
            }
            .addOnFailureListener {
                Log.d(DEBUG_TAG, "PurchaseDataSource -> $it")
            }
            .await()

        emit(purchases)
    }
}