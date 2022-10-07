package uwu.victoraso.storeapp.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class Wishlist(
    var id: Long = 0,
    var name: String = "",
    var itemCount: Int = 0,
    var wishlistedItems: List<CartProduct> = emptyList(),
) : Parcelable