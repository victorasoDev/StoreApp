package uwu.victoraso.storeapp.model

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import uwu.victoraso.storeapp.MainDestinations

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
    val imageUrl: String,
    val route: String
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
        name = "Genres",
        categories = listOf(
            SearchCategory(
                name = "Adventure",
                imageUrl = "https://images.wallpapersden.com/image/wxl-diablo-immortal-hd-2022-gaming_86692.jpg",
                route = "${MainDestinations.PRODUCT_LIST_ROUTE}/Adventure"
            ),
            SearchCategory(
                name = "Casual",
                imageUrl = "https://c4.wallpaperflare.com/wallpaper/207/907/686/stardew-valley-wallpaper-preview.jpg",
                route = "${MainDestinations.PRODUCT_LIST_ROUTE}/Casual"
            ),
            SearchCategory(
                name = "Exploration",
                imageUrl = "https://c4.wallpaperflare.com/wallpaper/836/656/845/outer-wilds-science-fiction-space-artwork-trees-hd-wallpaper-preview.jpg",
                route = "${MainDestinations.PRODUCT_LIST_ROUTE}/Exploration"
            ),
            SearchCategory(
                name = "Metroidvania",
                imageUrl = "https://wallpaperaccess.com/full/771507.png",
                route = "${MainDestinations.PRODUCT_LIST_ROUTE}/Metroidvania"
            ),
            SearchCategory(
                name = "Open-World",
                imageUrl = "https://cdn.mos.cms.futurecdn.net/SXsB2g4MumdofeWN7tm4jH.jpg",
                "${MainDestinations.PRODUCT_LIST_ROUTE}/Open-World"
            ),
            SearchCategory(
                name = "Rogue-Like",
                imageUrl = "https://c4.wallpaperflare.com/wallpaper/896/224/864/the-binding-of-isaac-rebirth-wallpaper-preview.jpg",
                "${MainDestinations.PRODUCT_LIST_ROUTE}/Rogue-Like"
            ),
            SearchCategory(
                name = "Simulation",
                imageUrl = "https://images2.alphacoders.com/582/thumb-1920-582521.png",
                route = "${MainDestinations.PRODUCT_LIST_ROUTE}/Simulation"
            ),
            SearchCategory(
                name = "Survival",
                imageUrl = "https://images6.alphacoders.com/107/1075749.png",
                route = "${MainDestinations.PRODUCT_LIST_ROUTE}/Survival"
            )
        )
    )
)

private val searchSuggestions = listOf(
    SearchSuggestionGroup(
        id = 0L,
        name = "Recent searches",
        suggestions = listOf(
            "red dead redemption",
            "the binding of",
            "outer wilds",
            "subnautica"
        )
    )
)