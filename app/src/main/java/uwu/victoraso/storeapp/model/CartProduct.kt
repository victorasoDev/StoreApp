package uwu.victoraso.storeapp.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class CartProduct(//TODO hacer val?
    var id: Long = 0,
    var name: String = "",
    var imageUrl: String = "",
    var price: Long = 0,
    var category: String = "",
    var wishlistId: Long = 0,
    var addDate: Long = 0
) : Parcelable