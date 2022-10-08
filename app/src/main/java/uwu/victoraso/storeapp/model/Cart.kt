package uwu.victoraso.storeapp.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import uwu.victoraso.storeapp.room.model.CartEntity

@Immutable
@Parcelize
data class Cart(
    var id: Long = 0,
    var name: String = "",
    var itemCount: Int = 0,
    var cartItems: List<CartProduct> = emptyList(),
) : Parcelable

fun Cart.asEntity() = CartEntity(
    id = this.id,
    name = this.name,
    itemCount = this.itemCount,
)