package uwu.victoraso.storeapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import uwu.victoraso.storeapp.room.dao.CartDao
import uwu.victoraso.storeapp.room.dao.CartProductDao
import uwu.victoraso.storeapp.room.model.CartEntity
import uwu.victoraso.storeapp.room.model.CartProductEntity

@Database(
    entities = [
        CartEntity::class,
        CartProductEntity::class
    ],
    version = 1,
    exportSchema = true,
)
abstract class StoreAppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun cartProductsDao(): CartProductDao
}