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
    var imageUrl: String = "",
    var price: Long = 0,
    var category: String = "",
    var cartId: Long = 0,
    var addDate: Long = 0
) : Parcelable

fun CartProduct.asEntity() = CartProductEntity(
    productId = productId,
    name = name,
    imageUrl = imageUrl,
    price = price,
    category = category,
    cartId = cartId,
    addDate = addDate
)