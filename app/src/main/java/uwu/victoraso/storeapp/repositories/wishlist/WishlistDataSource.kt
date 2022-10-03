package uwu.victoraso.storeapp.repositories.wishlist

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.Wishlist
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WishlistDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    /**
     * Wishlist a productID
     */
    fun wishlistToggle(productId: Long, userId: String, wishlist: Boolean) {
        try {
            val wishlistCollection = db.collection("wishlists").document(userId)
            wishlistCollection.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(DEBUG_TAG, "document != null")
                        if (!wishlist) wishlistCollection.update("product_ids", FieldValue.arrayUnion(productId))
                        else wishlistCollection.update("product_ids", FieldValue.arrayRemove(productId))
                    } else {
                        Log.d(DEBUG_TAG, "document == null")
                    }

                }
//            if (!wishlist) doc.update("product_ids", FieldValue.arrayUnion(productId))
//            else doc.update("product_ids", FieldValue.arrayRemove(productId))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isWishlisted(productId: Long, userId: String): Flow<Boolean> = flow {
        var exists = false
            db.collection("wishlists").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        for (id in document.data!!["product_ids"] as ArrayList<*>) {
                            if (productId == id) {
                                exists = true
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    //TODO
                }
                .await()
        emit(exists)
    }
}
