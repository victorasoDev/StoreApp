package uwu.victoraso.storeapp.room.model

import androidx.room.Embedded
import androidx.room.Relation
import uwu.victoraso.storeapp.model.CartProduct

/**
 * External data layer representation of a fully populated [CartProduct]
 */

data class PopulatedCartProduct(
    @Embedded
    val entity: CartProductEntity,
    @Relation(
        parentColumn = "cart_id",
        entityColumn = "id"
    )
    val wishlist: CartEntity
)

fun PopulatedCartProduct.asExternalModel() = CartProduct(
    productId = entity.productId,
    name = entity.name,
    imageUrl = entity.imageUrl,
    price = entity.price,
    category = entity.category,
    cartId = wishlist.id,
)
