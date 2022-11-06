package uwu.victoraso.storeapp.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class Product(
    var id: Long = 0,
    var name: String = "",
    var imageUrl: String = "",
    var price: Long = 0,
    var tagline: String = "",
    var categories: List<String> = emptyList(),
    var isWishlist: Boolean = false
) : Parcelable

/**
 * Static data
 */

val products = listOf(
    Product(
        id = 1,
        name = "Cupcake",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/pGM4sjt_BdQ",
        categories = listOf(""),
        price = 299
    ),
    Product(
        id = 1,
        name = "Cupcake",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/pGM4sjt_BdQ",
        categories = listOf(""),
        price = 299
    ),

)