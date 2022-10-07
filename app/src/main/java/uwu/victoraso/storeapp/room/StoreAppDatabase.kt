package uwu.victoraso.storeapp.room

import androidx.room.RoomDatabase
import uwu.victoraso.storeapp.room.dao.CartDao
import uwu.victoraso.storeapp.room.dao.CartProductDao

abstract class StoreAppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun cartProductsDao(): CartProductDao
}