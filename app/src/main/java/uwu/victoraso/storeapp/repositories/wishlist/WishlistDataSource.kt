package uwu.victoraso.storeapp.repositories.wishlist

import android.util.Log
import androidx.compose.runtime.snapshotFlow
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.Wishlist
import uwu.victoraso.storeapp.model.products
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
            val productReference = db.collection("products").document(productId.toString()).path
            val wishlistCollection = db.collection("wishlists").document(userId)
            wishlistCollection.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(DEBUG_TAG, "document != null")
                        if (wishlist) wishlistCollection.update("products", FieldValue.arrayUnion(productReference))
                        else wishlistCollection.update("products", FieldValue.arrayRemove(productReference))
                    } else {
                        Log.d(DEBUG_TAG, "document == null")
                    }

                }
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

    /**
     * Get products in user wishlist by userId
     */
    fun getWishlistByUserId(userId: String): Flow<MutableList<Product>> = flow {
        val productList = ArrayList<Product>()
        db.collection("wishlists").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                for (id in documentSnapshot.data!!["products"] as ArrayList<*>) {
                    CoroutineScope(Dispatchers.IO).launch {
                        db.document(id.toString())
                            .get()
                            .addOnSuccessListener { doc ->
                                val product = doc.toObject(Product::class.java)
                                if (product != null) productList.add(product)
                            }
                            .addOnFailureListener {
                                Log.d(DEBUG_TAG, "getWishlistByUserId -> ${it}")
                            }.await()
                    }
                }
            }
            .addOnFailureListener {
                Log.d(DEBUG_TAG, "WishlistDataSource -> ${it}")
            }
            .await()
        emit(productList)
    }.distinctUntilChanged()
}
