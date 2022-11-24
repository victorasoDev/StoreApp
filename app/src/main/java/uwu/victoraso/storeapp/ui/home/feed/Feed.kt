package uwu.victoraso.storeapp.ui.home.feed

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uwu.victoraso.storeapp.model.ProductCollection
import uwu.victoraso.storeapp.ui.components.ProductCollection
import uwu.victoraso.storeapp.ui.components.ProductImage
import uwu.victoraso.storeapp.ui.components.StoreAppDivider
import uwu.victoraso.storeapp.ui.components.StoreAppSurface
import uwu.victoraso.storeapp.ui.home.DestinationBar
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
                Column {
                    DestinationBar(onDestinationBarButtonClick = onProductCreate)
                    FeedContent(
                        productCollections = productCollections,
                        onProductClick = onProductClick,
                        onNavigateTo = onNavigateTo,
                    )
                }
            }
        }
        else -> Unit
    }

}

@Composable
private fun FeedContent(
    productCollections: List<ProductCollection>,
    onProductClick: (Long, String) -> Unit,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyColumnState = rememberLazyListState()

    Box(modifier = modifier) {
        LazyColumn (state = lazyColumnState) {
            item {
                FeedProductHeader()
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
}

@Composable
fun FeedProductHeader(
    modifier: Modifier = Modifier
) {
    ProductImage(
        imageUrl = "https://cdn.akamai.steamstatic.com/steam/apps/722230/header.jpg?t=1618852074",
        contentDescription = "header image",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
    )
}


fun getProductsCollections(feedUiState: FeedScreenUiState): List<ProductCollection> {
    val productCollections = ArrayList<ProductCollection>()
    if (feedUiState.processors is FeedUiState.Success) productCollections.add(feedUiState.processors.productCollection)
    if (feedUiState.motherboards is FeedUiState.Success) productCollections.add(feedUiState.motherboards.productCollection)
    if (feedUiState.graphicCards is FeedUiState.Success) productCollections.add(feedUiState.graphicCards.productCollection)
    if (feedUiState.storages is FeedUiState.Success) productCollections.add(feedUiState.storages.productCollection)
    if (feedUiState.coolingSystems is FeedUiState.Success) productCollections.add(feedUiState.coolingSystems.productCollection)
    if (feedUiState.rams is FeedUiState.Success) productCollections.add(feedUiState.rams.productCollection)
    if (feedUiState.laptops is FeedUiState.Success) productCollections.add(feedUiState.laptops.productCollection)
    if (feedUiState.builds is FeedUiState.Success) productCollections.add(feedUiState.builds.productCollection)
    if (feedUiState.monitors is FeedUiState.Success) productCollections.add(feedUiState.monitors.productCollection)
    if (feedUiState.mouses is FeedUiState.Success) productCollections.add(feedUiState.mouses.productCollection)
    if (feedUiState.keyboards is FeedUiState.Success) productCollections.add(feedUiState.keyboards.productCollection)

    return productCollections
}