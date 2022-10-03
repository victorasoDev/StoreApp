package uwu.victoraso.storeapp.repositories.wishlist

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WishlistRepository
@Inject
constructor(
    private val wishlistDataSource: WishlistDataSource,
) : ProductRepositoryInterface
{
    override fun wishlistToggle(productId: Long, userId: String, wishlist: Boolean) = wishlistDataSource.wishlistToggle(productId, userId, wishlist)

    override fun isWishlisted(productId: Long, userId: String): Flow<Boolean> = wishlistDataSource.isWishlisted(productId, userId)
}

sealed interface ProductRepositoryInterface {
    fun wishlistToggle(productId: Long, userId: String, wishlist: Boolean)
    fun isWishlisted(productId: Long, userId: String): Flow<Boolean>
//    fun getUserWishlist(userId: String): Flow<List<Product>>
}