package uwu.victoraso.storeapp.repositories.purchases

import com.google.firebase.firestore.FirebaseFirestore
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
}