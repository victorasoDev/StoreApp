package uwu.victoraso.storeapp.room

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uwu.victoraso.storeapp.room.dao.CartDao
import uwu.victoraso.storeapp.room.dao.CartProductDao

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesCartDao(
        database: StoreAppDatabase,
    ): CartDao = database.cartDao()

    @Provides
    fun providesCartProductsDao(
        database: StoreAppDatabase,
    ): CartProductDao = database.cartProductsDao()
}