package uwu.victoraso.storeapp.repositories.wishlist

import kotlinx.coroutines.flow.Flow
import uwu.victoraso.storeapp.model.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WishlistRepository
@Inject
constructor(
    private val wishlistDataSource: WishlistDataSource
) : ProductRepositoryInterface
{
    override fun wishlistToggle(product: Product, userId: String, wishlist: Boolean) = wishlistDataSource.wishlistToggle(product, userId, wishlist)

    override fun isWishlisted(productId: Long, userId: String): Flow<Boolean> = wishlistDataSource.isWishlisted(productId, userId)

    override fun getUserWishlist(userId: String): Flow<MutableList<Product>> = wishlistDataSource.getWishlistByUserId(userId)
}

sealed interface ProductRepositoryInterface {
    fun wishlistToggle(product: Product, userId: String, wishlist: Boolean)
    fun isWishlisted(productId: Long, userId: String): Flow<Boolean>
    fun getUserWishlist(userId: String): Flow<List<Product>>
}