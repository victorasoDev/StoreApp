package uwu.victoraso.storeapp.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import uwu.victoraso.storeapp.room.model.CartProductEntity

@Immutable
@Parcelize
data class CartProduct(//TODO hacer val?
    var productId: Long = 0,
    var name: String = "",
    var iconUrl: String = "",
    var imageUrl: String = "",
    var price: Long = 0,
    var category: String = "",
    var cartId: Long = 0,
    var addDate: Long = 0
) : Parcelable

fun CartProduct.asEntity() = CartProductEntity(
    productId = productId,
    name = name,
    iconUrl = iconUrl,
    imageUrl = imageUrl,
    price = price,
    category = category,
    cartId = cartId,
    addDate = addDate
)

fun CartProduct.fill(product: Product, cart: Cart): CartProduct { //TODO posible guarrada?
    productId = product.id
    name = product.name
    iconUrl = product.iconUrl
    imageUrl = product.imageUrl
    price = product.price
    category = product.category
    cartId = cart.id
    addDate = System.currentTimeMillis() / 1000
    return this
}