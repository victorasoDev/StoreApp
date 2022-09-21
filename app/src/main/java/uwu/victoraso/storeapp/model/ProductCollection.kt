package uwu.victoraso.storeapp.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.google.firebase.firestore.DocumentReference
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class ProductCollection(
    var id: Long,
    var name: String,
    var products: List<Product> = emptyList(),
    var type: CollectionType = CollectionType.Normal
) : Parcelable

enum class CollectionType { Normal, Highlight }

/**
 * Fake repo
 */

object ProductRepo {
    fun getProducts(): List<ProductCollection> = productCollections
    fun getProduct(productId: Long) = products.find { it.id == productId } //Super cool function
    fun getRelated(productId: Long) = related
    fun getInspiredByCart() = inspiredByCart
    fun getFilters() = StoreAppFilters
    fun getPriceFilters() = priceFilters
    fun getCart() = cart
    fun getSortFilters() = sortFilters
    fun getCategoryFilters() = categoryFilters
    fun getSortDefault() = sortDefault
    fun getLifeStyleFilters() = lifeStyleFilters
}

/**
 * Static data - Change name as soon as I can
 */

private val tastyTreats = ProductCollection(
    id = 1L,
    name = "Processors",
    type = CollectionType.Highlight,
    products = products.subList(0, 13)
)

private val popular = ProductCollection(
    id = 1L,
    name = "Popular on StoreApp",
    type = CollectionType.Highlight,
    products = products.subList(14, 19)
)

private val wfhFavs = tastyTreats.copy(
    id = 3L,
    name = "WFH favourites"
)

private val newlyAdded = popular.copy(
    id = 4L,
    name = "Newly Added"
)

private val exclusive = tastyTreats.copy(
    id = 5L,
    name = "Only on Jetsnack"
)

private val also = tastyTreats.copy(
    id = 6L,
    name = "Customers also bought"
)

private val inspiredByCart = tastyTreats.copy(
    id = 7L,
    name = "Inspired by your cart"
)

private val productCollections = listOf(
    tastyTreats,
    popular,
    wfhFavs,
    newlyAdded,
    exclusive
)

private val related = listOf(
    also,
    popular
)

private val cart = listOf(
    OrderLine(products[4], 2),
    OrderLine(products[6], 3),
    OrderLine(products[8], 1)
)

@Immutable
data class OrderLine(
    val product: Product,
    val count: Int
)