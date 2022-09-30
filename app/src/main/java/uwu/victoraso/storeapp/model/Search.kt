package uwu.victoraso.storeapp.model

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object SearchRepo {
    fun getCategories(): List<SearchCategoryCollection> = searchCategoryCollections
    fun getSuggestions(): List<SearchSuggestionGroup> = searchSuggestions

    suspend fun search(query: String): List<Product> = withContext(Dispatchers.Default) {
        delay(200L)
        products.filter { it.name.contains(query) }
    }
}

@Immutable
data class SearchCategoryCollection(
    val id: Long,
    val name: String,
    val categories: List<SearchCategory>
)

@Immutable
data class SearchCategory(
    val name: String,
    val imageUrl: String
)

@Immutable
data class SearchSuggestionGroup(
    val id: Long,
    val name: String,
    val suggestions: List<String>
)

/**
 * Static data
 */

private val searchCategoryCollections = listOf(
    SearchCategoryCollection(
        id = 0L,
        name = "PC",
        categories = listOf(
            SearchCategory(
                name = "Builds",
                imageUrl = "https://source.unsplash.com/UsSdMZ78Q3E"
            ),
            SearchCategory(
                name = "Laptops",
                imageUrl = "https://source.unsplash.com/SfP1PtM9Qa8"
            ),
        )
    ),
    SearchCategoryCollection(
        id = 1L,
        name = "Components",
        categories = listOf(
            SearchCategory(
                name = "Processors",
                imageUrl = "https://source.unsplash.com/7meCnGCJ5Ms"
            ),
            SearchCategory(
                name = "Graphics Cards",
                imageUrl = "https://source.unsplash.com/m741tj4Cz7M"
            ),
            SearchCategory(
                name = "Motherboards",
                imageUrl = "https://source.unsplash.com/dt5-8tThZKg"
            ),
            SearchCategory(
                name = "RAMs",
                imageUrl = "https://source.unsplash.com/ReXxkS1m1H0"
            ),
            SearchCategory(
                name = "Cooling Systems",
                imageUrl = "https://source.unsplash.com/IGfIGP5ONV0"
            )
        )
    ),
    SearchCategoryCollection(
        id = 2L,
        name = "Peripherals",
        categories = listOf(
            SearchCategory(
                name = "Mouses",
                imageUrl = "https://source.unsplash.com/UsSdMZ78Q3E"
            ),
            SearchCategory(
                name = "Monitors",
                imageUrl = "https://source.unsplash.com/SfP1PtM9Qa8"
            ),
            SearchCategory(
                name = "Keyboards",
                imageUrl = "https://source.unsplash.com/SfP1PtM9Qa8"
            )
        )
    )
)

private val searchSuggestions = listOf(
    SearchSuggestionGroup(
        id = 0L,
        name = "Recent searches",
        suggestions = listOf(
            "MSI 3080",
            "intel core i7"
        )
    ),
    SearchSuggestionGroup(
        id = 1L,
        name = "Popular searches",
        suggestions = listOf(
            "Processors",
            "Graphic Cards",
            "Builds",
            "Laptops",
            "Keyboards",
            "Monitors"
        )
    )
)