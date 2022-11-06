package uwu.victoraso.storeapp.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
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
    fun getFilters() = StoreAppFilters
}

/**
 * Static data - Change name as soon as I can
 */