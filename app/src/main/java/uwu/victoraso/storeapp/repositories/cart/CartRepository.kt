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

    override suspend fun insertCart(entities: List<CartEntity>) { cartDao.insertOrIgnoreCart(entities) }

    override suspend fun updateCart(entities: List<CartEntity>) { cartDao.updateCart(entities) }

    override suspend fun deleteCart(ids: List<String>) { cartDao.deleteCarts(ids) }

    override suspend fun insertCartProduct(entity: CartProductEntity) { cartProductDao.insertOrIgnoreCartProduct(entity) }

    override suspend fun deleteCartProduct(productId: String, cartId: String) { cartProductDao.deleteCartProduct(productId, cartId) }
}


sealed interface CartRepositoryInterface {
    /**
     * [Cart] CRUD
     */
    fun getCartsStream(): Flow<List<Cart>>
    fun getCartByIdStream(id: String): Flow<Cart>
    suspend fun insertCart(entities: List<CartEntity>)
    suspend fun updateCart(entities: List<CartEntity>)
    suspend fun deleteCart(ids: List<String>)

    /**
     * [CartProduct] CRUD
     */
    suspend fun insertCartProduct(entity: CartProductEntity)
    suspend fun deleteCartProduct(productId: String, cartId: String)
}