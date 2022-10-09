package uwu.victoraso.storeapp.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uwu.victoraso.storeapp.room.model.PopulatedCart
import uwu.victoraso.storeapp.room.model.CartEntity

/**
 * DAO for [Wishlist] and [CartEntity] access
 */
@Dao
interface CartDao {
    @Transaction
    @Query(
        value = """
            SELECT * FROM cart
            ORDER BY id DESC
    """
    )
    fun getCartsStream(): Flow<List<PopulatedCart>>

    @Transaction
    @Query(
        value = """
            SELECT * FROM cart
            WHERE id == :id
    """
    )
    fun getCartByIdStream(id: String): Flow<PopulatedCart>

    /**
     * Check if has a row stored with same [cartId]
     */
    @Query("SELECT EXISTS(SELECT * FROM cart_products WHERE cart_id = :cartId)")
    fun isCartStored(cartId: String): Boolean

    /**
     * Inserts [entities] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreCart(entity: CartEntity)

    /**
     * Updates [entities] in the db that match the primary key, and no-ops if they don't
     */
    @Update
    suspend fun updateCart(entities: List<CartEntity>)

    /**
     * Inserts or updates [cartEntities] in the db under the specified primary keys
     */
//    @Transaction
//    suspend fun upsertCart(cartEntities: List<CartEntity>) = upsert(
//        items = cartEntities,
//        insertMany = ::insertOrIgnoreCart,
//        updateMany = ::updateCart
//    )

    /**
     * Deletes rows in the db matching the specified [ids]
     */
    @Query(
        value = """
            DELETE FROM cart
            WHERE id in (:ids)
        """
    )
    suspend fun deleteCarts(ids: List<String>)
}