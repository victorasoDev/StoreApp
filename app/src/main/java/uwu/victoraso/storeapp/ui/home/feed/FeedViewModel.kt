package uwu.victoraso.storeapp.ui.home.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import uwu.victoraso.storeapp.ds.Result
import uwu.victoraso.storeapp.ds.asResult
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.ProductCollection
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import javax.inject.Inject

@HiltViewModel
class FeedViewModel
@Inject
constructor(
    productRepository: ProductRepository,
) : ViewModel() {

    private val processorProductsStream: Flow<Result<List<Product>>> =
        productRepository.getProductsByCategory("Processors")
            .asResult()

    private val graphicsCardsProductsStream: Flow<Result<List<Product>>> =
        productRepository.getProductsByCategory("Graphic Cards")
            .asResult()

    private val ramsProductsStream: Flow<Result<List<Product>>> =
        productRepository.getProductsByCategory("RAMs")
            .asResult()

    val uiState: StateFlow<FeedScreenUiState> =
        combine(
            processorProductsStream,
            graphicsCardsProductsStream,
            ramsProductsStream,
        ) { processorProductsResult, graphicsCardsProductsResult, ramsProductsResult->
            /** Get processor list **/
            val processors: FeedUiState =
                when (processorProductsResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 1L,
                            name = "Processors",
                            products = processorProductsResult.data
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }
            /** Get graphic cards list **/
            val graphicCards: FeedUiState =
                when (graphicsCardsProductsResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 2L,
                            name = "Graphic Cards",
                            products = graphicsCardsProductsResult.data
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }
            /** Get rams list **/
            val rams: FeedUiState =
                when (ramsProductsResult) {
                    is Result.Success -> FeedUiState.Success(
                        ProductCollection(
                            id = 3L,
                            name = "RAMs",
                            products = ramsProductsResult.data
                        )
                    )
                    is Result.Loading -> FeedUiState.Loading
                    is Result.Error -> FeedUiState.Error
                }
            FeedScreenUiState(processors, graphicCards, rams)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FeedScreenUiState(
                    processors = FeedUiState.Loading,
                    graphicCards = FeedUiState.Loading,
                    rams = FeedUiState.Loading,
                )
            )

}

sealed interface FeedUiState {
    data class Success(val productCollection: ProductCollection) : FeedUiState
    object Error : FeedUiState
    object Loading : FeedUiState
}

data class FeedScreenUiState(
    val processors: FeedUiState,
    val graphicCards: FeedUiState,
    val rams: FeedUiState,
)