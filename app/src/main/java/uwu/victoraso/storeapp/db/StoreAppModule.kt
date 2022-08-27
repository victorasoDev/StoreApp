package uwu.victoraso.storeapp.db

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StoreAppModule {

    @Provides
    @Singleton
    fun provideFirestoreInstance() = FirebaseFirestore.getInstance()

//    @Provides
//    @Singleton
//    fun provideProductList(
//        firestore: FirebaseFirestore
//    ) = firestore.collection("products")
//
//    @Provides
//    @Singleton
//    fun provideProductCollection(
//        firestore: FirebaseFirestore
//    ) = firestore.collection("productCollection")
}