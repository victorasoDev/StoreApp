package uwu.victoraso.storeapp.ui.home.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import uwu.victoraso.storeapp.ds.Result
import uwu.victoraso.storeapp.ds.asResult
import uwu.victoraso.storeapp.model.CollectionType
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.ProductCollection
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import javax.inject.Inject

@HiltViewModel
class FeedViewModel
@Inject
constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val processorProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Processors")
    private val motherboardsProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Motherboards")
    private val graphicsCardsProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Graphic Cards")
    private val storagesCardsProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Storages")
    private val coolingSystemCardsProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Cooling systems")
    private val ramsProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("RAMs")
    private val laptopsProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Laptops")
    private val buildsProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Builds")
    private val monitorsProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Monitors")
    private val mousesProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Mouses")
    private val keyboardsProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Keyboards")

    val uiState: StateFlow<FeedScreenUiState> =
        feedCombine(
            processorProductsStream,
            motherboardsProductsStream,
            graphicsCardsProductsStream,
            storagesCardsProductsStream,
            coolingSystemCardsProductsStream,
            ramsProductsStream,
            laptopsProductsStream,
            buildsProductsStream,
            monitorsProductsStream,
            mousesProductsStream,
            keyboardsProductsStream,
        ) { processorResult, motherboardsResult, graphicsCardsResult, storagesResult, coolingSystemsResult,
            ramsResult, laptopsResult, buildsResult, monitorsResult, mousesResult, keyboardsResult ->
            /** Get processor list **/
            val processors: FeedUiState =
                when (processorResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 1L,
                            name = "Processors",
                            products = processorResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get motherboards list **/
            val motherboards: FeedUiState =
                when (motherboardsResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 2L,
                            name = "Motherboards",
                            products = motherboardsResult.data,
                            type = CollectionType.Normal
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get graphic cards list **/
            val graphicCards: FeedUiState =
                when (graphicsCardsResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 3L,
                            name = "Graphic Cards",
                            products = graphicsCardsResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get storages list **/
            val storages: FeedUiState =
                when (storagesResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 4L,
                            name = "Storages",
                            products = storagesResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get cooling systems list **/
            val coolingSystems: FeedUiState =
                when (coolingSystemsResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 5L,
                            name = "Cooling Systems",
                            products = coolingSystemsResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get rams list **/
            val rams: FeedUiState =
                when (ramsResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 6L,
                            name = "RAMs",
                            products = ramsResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get laptops list **/
            val laptops: FeedUiState =
                when (laptopsResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 7L,
                            name = "Laptops",
                            products = laptopsResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get builds list **/
            val builds: FeedUiState =
                when (buildsResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 8L,
                            name = "Builds",
                            products = buildsResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get monitors list **/
            val monitors: FeedUiState =
                when (monitorsResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 9L,
                            name = "Monitors",
                            products = monitorsResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get mouses list **/
            val mouses: FeedUiState =
                when (mousesResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 10L,
                            name = "Mouses",
                            products = mousesResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get keyboards list **/
            val keyboards: FeedUiState =
                when (keyboardsResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 11L,
                            name = "Keyboards",
                            products = keyboardsResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }
            FeedScreenUiState(processors, motherboards, graphicCards, storages, coolingSystems, rams, laptops, builds, monitors, mouses, keyboards)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FeedScreenUiState(
                    processors = FeedUiState.Loading,
                    motherboards = FeedUiState.Loading,
                    graphicCards = FeedUiState.Loading,
                    storages = FeedUiState.Loading,
                    coolingSystems = FeedUiState.Loading,
                    rams = FeedUiState.Loading,
                    laptops = FeedUiState.Loading,
                    builds = FeedUiState.Loading,
                    monitors = FeedUiState.Loading,
                    mouses = FeedUiState.Loading,
                    keyboards = FeedUiState.Loading,
                )
            )

    private fun getStreamResultByCategory(category: String): Flow<Result<List<Product>>> {
        return productRepository.getProductsByCategory(category)
            .asResult()
    }
}

private inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> feedCombine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    flow10: Flow<T10>,
    flow11: Flow<T11>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) -> R
): Flow<R> {
    return combine(flow, flow2, flow3, flow4, flow5, flow6, flow7, flow8, flow9, flow10, flow11) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[4] as T5,
            args[5] as T6,
            args[6] as T7,
            args[7] as T8,
            args[8] as T9,
            args[9] as T10,
            args[10] as T11,
        )
    }
}

sealed interface FeedUiState {
    data class Success(val productCollection: ProductCollection) : FeedUiState
    object Error : FeedUiState
    object Loading : FeedUiState
}

data class FeedScreenUiState(
    val processors: FeedUiState,
    val motherboards: FeedUiState,
    val graphicCards: FeedUiState,
    val storages: FeedUiState,
    val coolingSystems: FeedUiState,
    val rams: FeedUiState,
    val laptops: FeedUiState,
    val builds: FeedUiState,
    val monitors: FeedUiState,
    val mouses: FeedUiState,
    val keyboards: FeedUiState,
)