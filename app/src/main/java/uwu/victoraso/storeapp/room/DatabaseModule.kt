package uwu.victoraso.storeapp.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesStoreAppDatabase(
        @ApplicationContext context: Context,
    ): StoreAppDatabase = Room.databaseBuilder(
        context,
        StoreAppDatabase::class.java,
        "storeapp-database"
    ).build()
}