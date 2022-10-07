package uwu.victoraso.storeapp.repositories.cart

import kotlinx.coroutines.flow.Flow
import uwu.victoraso.storeapp.repositories.Syncable


sealed interface CartRepository : Syncable {
    /**
     * Returns available news resources as a stream.
     */
    fun getCartsStream(): Flow<List<CartRepository>>

    /**
     *
     */


    /**
     *
     */


    /**
     *
     */


    /**
     *
     */


    /**
     *
     */


    /**
     *
     */


    /**
     *
     */
}