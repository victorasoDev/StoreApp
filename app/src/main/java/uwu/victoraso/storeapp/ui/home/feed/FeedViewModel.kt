package uwu.victoraso.storeapp.ui.home.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import uwu.victoraso.storeapp.model.CollectionType
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.ProductCollection
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import javax.inject.Inject

@HiltViewModel
class FeedViewModel
@Inject
constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val adventureProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Adventure")
    private val openWorldProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Open-World")
    private val survivalCardsProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Survival")
    private val explorationCardsProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Exploration")
    private val rogueLikeCardsProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Rogue-Like")
    private val metroidvaniaProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Metroidvania")
    private val horrorProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Horror")
    private val simulationProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Simulation")
    private val casualProductsStream: Flow<Result<List<Product>>> = getStreamResultByCategory("Casual")

    val uiState: StateFlow<FeedScreenUiState> =
        feedCombine(
            adventureProductsStream,
            openWorldProductsStream,
            survivalCardsProductsStream,
            explorationCardsProductsStream,
            rogueLikeCardsProductsStream,
            metroidvaniaProductsStream,
            horrorProductsStream,
            simulationProductsStream,
            casualProductsStream,
        ) { adventureResult, openWorldResult, survivalResult, explorationResult, rogueLikeResult,
            metroidvaniaResult, horrorResult, simulationResult, casualResult ->
            /** Get processor list **/
            val processors: FeedUiState =
                when (adventureResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 1L,
                            name = "Adventure",
                            products = adventureResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get motherboards list **/
            val motherboards: FeedUiState =
                when (openWorldResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 2L,
                            name = "Open-World",
                            products = openWorldResult.data,
                            type = CollectionType.Normal
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get graphic cards list **/
            val graphicCards: FeedUiState =
                when (survivalResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 3L,
                            name = "Survival",
                            products = survivalResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get storages list **/
            val storages: FeedUiState =
                when (explorationResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 4L,
                            name = "Exploration",
                            products = explorationResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get cooling systems list **/
            val coolingSystems: FeedUiState =
                when (rogueLikeResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 5L,
                            name = "Rogue-Like",
                            products = rogueLikeResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get rams list **/
            val rams: FeedUiState =
                when (metroidvaniaResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 6L,
                            name = "Metroidvania",
                            products = metroidvaniaResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get laptops list **/
            val laptops: FeedUiState =
                when (horrorResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 7L,
                            name = "Horror",
                            products = horrorResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get builds list **/
            val builds: FeedUiState =
                when (simulationResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 8L,
                            name = "Simulation",
                            products = simulationResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }

            /** Get monitors list **/
            val monitors: FeedUiState =
                when (casualResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 9L,
                            name = "Casual",
                            products = casualResult.data,
                            type = CollectionType.Highlight
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }
            FeedScreenUiState(processors, motherboards, graphicCards, storages, coolingSystems, rams, laptops, builds, monitors)
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
                )
            )

    private fun getStreamResultByCategory(category: String): Flow<Result<List<Product>>> {
        return productRepository.getProductsByCategory(category)
            .asResult()
    }
}

private inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> feedCombine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R
): Flow<R> {
    return combine(flow, flow2, flow3, flow4, flow5, flow6, flow7, flow8, flow9) { args: Array<*> ->
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
            args[8] as T9
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
)