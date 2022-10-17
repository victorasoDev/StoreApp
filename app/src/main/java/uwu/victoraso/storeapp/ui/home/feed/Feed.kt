package uwu.victoraso.storeapp.ui.home.feed

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uwu.victoraso.storeapp.model.ProductCollection
import uwu.victoraso.storeapp.ui.components.ProductCollection
import uwu.victoraso.storeapp.ui.components.StoreAppCircularIndicator
import uwu.victoraso.storeapp.ui.components.StoreAppDivider
import uwu.victoraso.storeapp.ui.components.StoreAppSurface
import uwu.victoraso.storeapp.ui.home.DestinationBar
import uwu.victoraso.storeapp.ui.home.FilterScreen
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun Feed(
    onProductClick: (Long, String) -> Unit,
    onNavigateTo: (String) -> Unit,
    onProductCreate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val feedUiState: FeedScreenUiState by viewModel.uiState.collectAsStateWithLifecycle()

    val productsCollections = getProductsCollections(feedUiState)

    Feed(
        feedUiState = feedUiState,
        productCollections = productsCollections,
        onProductClick = onProductClick,
        onNavigateTo = onNavigateTo,
        onProductCreate = onProductCreate,
        modifier = modifier,
    )
}

@Composable
private fun Feed(
    feedUiState: FeedScreenUiState,
    productCollections: List<ProductCollection>,
    onProductClick: (Long, String) -> Unit,
    onNavigateTo: (String) -> Unit,
    onProductCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (feedUiState.processors) {
        is FeedUiState.Success -> {
            Log.d(DEBUG_TAG, "Feed Success -> ${feedUiState.processors.productCollection.products.size}")
            StoreAppSurface(modifier = modifier.fillMaxSize()) {
                Box {
                    ProductCollectionList(
                        productCollections = productCollections,
                        onProductClick = onProductClick,
                        onNavigateTo = onNavigateTo,
                    )
                    DestinationBar(onDestinationBarButtonClick = onProductCreate)
                }
            }
        }
        is FeedUiState.Loading -> {
            Log.d(DEBUG_TAG, "Feed Loading")
            StoreAppCircularIndicator()
        }
        is FeedUiState.Error -> {
            Log.d(DEBUG_TAG, "Feed Error")
        }
    }

}

fun getProductsCollections(feedUiState: FeedScreenUiState): List<ProductCollection> {
    val productCollection = ArrayList<ProductCollection>()
    if (feedUiState.processors is FeedUiState.Success) productCollection.add(feedUiState.processors.productCollection)
    if (feedUiState.motherboards is FeedUiState.Success) productCollection.add(feedUiState.motherboards.productCollection)
    if (feedUiState.graphicCards is FeedUiState.Success) productCollection.add(feedUiState.graphicCards.productCollection)
    if (feedUiState.storages is FeedUiState.Success) productCollection.add(feedUiState.storages.productCollection)
    if (feedUiState.coolingSystems is FeedUiState.Success) productCollection.add(feedUiState.coolingSystems.productCollection)
    if (feedUiState.rams is FeedUiState.Success) productCollection.add(feedUiState.rams.productCollection)
    if (feedUiState.laptops is FeedUiState.Success) productCollection.add(feedUiState.laptops.productCollection)
    if (feedUiState.builds is FeedUiState.Success) productCollection.add(feedUiState.builds.productCollection)
    if (feedUiState.monitors is FeedUiState.Success) productCollection.add(feedUiState.monitors.productCollection)
    if (feedUiState.mouses is FeedUiState.Success) productCollection.add(feedUiState.mouses.productCollection)
    if (feedUiState.keyboards is FeedUiState.Success) productCollection.add(feedUiState.keyboards.productCollection)

    return productCollection
}

@Composable
private fun ProductCollectionList(
    productCollections: List<ProductCollection>,
    onProductClick: (Long, String) -> Unit,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var filtersVisible by rememberSaveable { mutableStateOf(false) }
    val lazyColumnState = rememberLazyListState()

    Box(modifier = modifier) {
        LazyColumn (state = lazyColumnState) {
            item {
                Spacer(
                    modifier = Modifier.windowInsetsTopHeight(
                        WindowInsets.statusBars.add(WindowInsets(top = 56.dp))
                    )
                )
            }
            itemsIndexed(productCollections) { index, productCollection ->
                if (index > 0) {
                    StoreAppDivider(thickness = 2.dp)
                }

                ProductCollection(
                    productCollection = productCollection,
                    onProductClick = onProductClick,
                    onNavigateTo = onNavigateTo,
                    index = index
                )
            }
        }
    }
    AnimatedVisibility(
        visible = filtersVisible,
        enter = slideInVertically() + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkHorizontally() + fadeOut()
    ) {
        FilterScreen(
            onDismiss = { filtersVisible = false }
        )
    }
}