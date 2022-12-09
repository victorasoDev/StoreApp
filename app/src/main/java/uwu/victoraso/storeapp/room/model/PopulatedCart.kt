package uwu.victoraso.storeapp.room.model

import androidx.room.Embedded
import androidx.room.Relation
import uwu.victoraso.storeapp.model.Cart

/**
 * External data layer representation of an [Cart]
 */

data class PopulatedCart(
    @Embedded
    val entity: CartEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "cart_id"
    )
    val productItems: List<CartProductEntity>
)

fun PopulatedCart.asExternalModel() = Cart(
    id = entity.id,
    name = entity.name,
    itemCount = entity.itemCount,
    cartItems = productItems.map(CartProductEntity::asExternalModel)
)
