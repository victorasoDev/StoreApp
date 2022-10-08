package uwu.victoraso.storeapp.repositories

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uwu.victoraso.storeapp.repositories.cart.CartRepository
import uwu.victoraso.storeapp.repositories.products.ProductDataSource
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import uwu.victoraso.storeapp.repositories.wishlist.WishlistDataSource
import uwu.victoraso.storeapp.repositories.wishlist.WishlistRepository
import uwu.victoraso.storeapp.room.dao.CartDao
import uwu.victoraso.storeapp.room.dao.CartProductDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(
        database: FirebaseFirestore,
        productDataSource: ProductDataSource
    ): ProductRepository {
        return ProductRepository(database, productDataSource)
    }

    @Provides
    @Singleton
    fun provideWishlistRepository(
        wishlistDataSource: WishlistDataSource
    ): WishlistRepository {
        return WishlistRepository(wishlistDataSource)
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        cartDao: CartDao,
        cartProductDao: CartProductDao
    ): CartRepository {
        return CartRepository(cartProductDao, cartDao)
    }
}