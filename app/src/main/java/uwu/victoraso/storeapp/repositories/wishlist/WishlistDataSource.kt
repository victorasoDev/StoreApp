package uwu.victoraso.storeapp.repositories.wishlist

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.ProductCollection
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WishlistDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    /**
     * Wishlist a productID
     */
    fun wishlistToggle(product: Product, userId: String, wishlist: Boolean) {
        try {
            val productReference = db.collection("products").document(product.id.toString()).path
            val wishlistCollection = db.collection("wishlists").document(userId)
            wishlistCollection.get()
                .addOnSuccessListener { document ->
                    if (document.data != null) {
                        if (wishlist) wishlistCollection.update("products", FieldValue.arrayUnion(product))
                        else wishlistCollection.update("products", FieldValue.arrayRemove(product))
                    } else {
                        Log.d(DEBUG_TAG, "elseee")
                        db.collection("wishlists").document(userId).set(ProductCollection(0, userId))
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
                if (document.data != null) {
                    for (id in document.data!!["products"] as ArrayList<*>) {
                        val idFormatted = (id as HashMap<*, *>)["id"] as Long
                        if (productId == idFormatted) {
                            exists = true
                        }
                    }
                }
            }
            .addOnFailureListener { }
            .await()
        emit(exists)
    }

    /**
     * Get products in user wishlist by userId
     */
    fun getWishlistByUserId(userId: String): Flow<MutableList<Product>> = flow {
        val productList = ArrayList<Product>()
        db.collection("wishlists")
            .whereEqualTo("name", userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                CoroutineScope(Dispatchers.IO).launch {
                    if (!documentSnapshot.isEmpty) {
                        for (p in documentSnapshot) {
                            val products = p.data["products"] as ArrayList<*>
                            products.map {
                                val product = it as HashMap<*, *>
                                productList.add(
                                    Product(
                                        id = product["id"] as Long,
                                        iconUrl = product["iconUrl"] as String,
                                        name = product["name"] as String,
                                        price = product["price"] as Long
                                    )
                                )
                            }
                            Log.d("asd", p.data["products"].toString())
                        }
                    }
                }
            }
            .addOnFailureListener {
            }
            .await()
        emit(productList)
    }.flowOn(Dispatchers.IO)
}
