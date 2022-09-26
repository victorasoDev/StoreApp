package uwu.victoraso.storeapp.model.service

import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class StorageServiceImpl @Inject constructor() : StorageService {
    private var listenerRegistration: ListenerRegistration? = null

    override fun addListener(
        userId: String,
        onDocumentEvent: (Boolean, String) -> Unit,
        onError: (Throwable) -> Unit
    ) {

    }

    override fun removeListener() {
        listenerRegistration?.remove()
    }

    override fun getTask( //TODO: getWishlist?
        taskId: String,
        onError: (Throwable) -> Unit,
        onSuccess: (String) -> Unit
    ) {

    }

    override fun saveTask(task: String, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(WISHLIST_COLLECTION)
            .add(task)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun updateTask(task: String, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(WISHLIST_COLLECTION)
            .document(task)
            .set(task)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun deleteTask(taskId: String, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(WISHLIST_COLLECTION)
            .document(taskId)
            .delete()
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun deleteAllForUser(userId: String, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(WISHLIST_COLLECTION)
            .whereEqualTo(USER_ID, userId)
            .get()
            .addOnFailureListener { error -> onResult(error) }
            .addOnSuccessListener { result ->
                for (document in result) document.reference.delete()
                onResult(null)
            }
    }

    override fun updateUserId(
        oldUserId: String,
        newUserId: String,
        onResult: (Throwable?) -> Unit
    ) {
        Firebase.firestore
            .collection(WISHLIST_COLLECTION)
            .whereEqualTo(USER_ID, oldUserId)
            .get()
            .addOnFailureListener { error -> onResult(error) }
            .addOnSuccessListener { result ->
                for (document in result) document.reference.update(USER_ID, newUserId)
                onResult(null)
            }
    }

    companion object {
        private const val WISHLIST_COLLECTION = "wishlists"
        private const val USER_ID = "userId"
    }
}

sealed interface StorageService {
    fun addListener(
        userId: String,
        onDocumentEvent: (Boolean, String) -> Unit,//TODO: guardar Producto en wishlist?
        onError: (Throwable) -> Unit
    )

    fun removeListener()
    fun getTask(taskId: String, onError: (Throwable) -> Unit, onSuccess: (String) -> Unit) //TODO: guardar Producto en wishlist?
    fun saveTask(task: String, onResult: (Throwable?) -> Unit) //TODO: guardar Producto en wishlist?
    fun updateTask(task: String, onResult: (Throwable?) -> Unit)//TODO: guardar Producto en wishlist?
    fun deleteTask(taskId: String, onResult: (Throwable?) -> Unit)
    fun deleteAllForUser(userId: String, onResult: (Throwable?) -> Unit)
    fun updateUserId(oldUserId: String, newUserId: String, onResult: (Throwable?) -> Unit)
}