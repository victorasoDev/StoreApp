package uwu.victoraso.storeapp.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uwu.victoraso.storeapp.room.model.PopulatedCartProduct
import uwu.victoraso.storeapp.room.model.CartProductEntity

/**
 * DAO for [WishlistProduct] and [CartProductEntity] access
 */
@Dao
interface CartProductDao {
    @Transaction
    @Query(
        value = """
            SELECT * FROM cart_products
            ORDER BY addDate DESC
    """
    )
    fun getCartProductsStream(): Flow<List<PopulatedCartProduct>>

    /**
     * Check if has a row stored with same [productId] && [cartId] exists
     */
    @Query("SELECT EXISTS(SELECT * FROM cart_products WHERE product_id = :productId AND cart_id = :cartId)")
    fun isCartProductStored(productId: String, cartId: String): Boolean

    /**
     * Inserts [entities] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreCartProduct(entity: CartProductEntity)

    /**
     * Updates [entities] in the db that match the primary key, and no-ops if they don't
     */
    @Update
    suspend fun updateCartProducts(entities: List<CartProductEntity>)

//    /**
//     * Inserts or updates [wishlistProductEntities] in the db under the specified primary keys
//     */
//    @Transaction
//    suspend fun upsertCartProducts(wishlistProductEntities: List<CartProductEntity>) = upsert(
//        items = wishlistProductEntities,
//        insertMany = ::insertOrIgnoreCartProduct,
//        updateMany = ::updateCartProducts
//    )
    /**
     * Deletes rows in the db matching the specified [ids]
     */
    @Query(
        value = """
            DELETE FROM cart_products
            WHERE product_id in (:productId) AND cart_id in (:cartId)
        """
    )
    suspend fun deleteCartProduct(productId: String, cartId: String)
}