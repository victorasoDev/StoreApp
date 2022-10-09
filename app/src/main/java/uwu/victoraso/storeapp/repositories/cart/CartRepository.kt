package uwu.victoraso.storeapp.repositories.cart

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uwu.victoraso.storeapp.model.Cart
import uwu.victoraso.storeapp.room.dao.CartDao
import uwu.victoraso.storeapp.room.dao.CartProductDao
import uwu.victoraso.storeapp.room.model.*
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartProductDao: CartProductDao,
    private val cartDao: CartDao
): CartRepositoryInterface {

    override fun getCartsStream(): Flow<List<Cart>> =
        cartDao.getCartsStream()
            .map { it.map(PopulatedCart::asExternalModel) }

    override fun getCartByIdStream(id: String): Flow<Cart> =
        cartDao.getCartByIdStream(id).map(PopulatedCart::asExternalModel)

    override suspend fun isCartStored(cartId: String, cartName: String): Boolean = cartDao.isCartStored(cartId, cartName)

    override suspend fun insertCart(entity: CartEntity) {
        if (!isCartStored(entity.id.toString(), entity.name)) cartDao.insertOrIgnoreCart(entity)
    }

    override suspend fun updateCart(entities: List<CartEntity>) { cartDao.updateCart(entities) }

    override suspend fun deleteCart(ids: List<String>) { cartDao.deleteCarts(ids) }

    override suspend fun isCartProductStored(productId: String, cartId: String): Boolean = cartProductDao.isCartProductStored(productId, cartId)

    override suspend fun insertCartProduct(entity: CartProductEntity) {
        if (!isCartProductStored(entity.productId.toString(), entity.cartId.toString())) cartProductDao.insertOrIgnoreCartProduct(entity)
    } //TODO: Devolver un boolean para mostrar un mensaje en viewHolder

    override suspend fun deleteCartProduct(productId: String, cartId: String) { cartProductDao.deleteCartProduct(productId, cartId) }
}


sealed interface CartRepositoryInterface {
    /**
     * [Cart] CRUD
     */
    fun getCartsStream(): Flow<List<Cart>>
    fun getCartByIdStream(id: String): Flow<Cart>
    suspend fun isCartStored(cartId: String, cartName: String): Boolean
    suspend fun insertCart(entity: CartEntity)
    suspend fun updateCart(entities: List<CartEntity>)
    suspend fun deleteCart(ids: List<String>)

    /**
     * [CartProduct] CRUD
     */
    suspend fun isCartProductStored(productId: String, cartId: String): Boolean
    suspend fun insertCartProduct(entity: CartProductEntity)
    suspend fun deleteCartProduct(productId: String, cartId: String)
}