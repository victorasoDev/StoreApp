package uwu.victoraso.storeapp.repositories

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(
        database: FirebaseFirestore
    ): ProductRepository {
        return ProductRepository(database)
    }
}