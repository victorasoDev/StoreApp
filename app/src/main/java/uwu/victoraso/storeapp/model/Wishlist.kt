package uwu.victoraso.storeapp.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class Wishlist(
    var id: Long = 0,
    var userId: String = "",
    var wishlistedItems: List<Long> = emptyList(),
) : Parcelable