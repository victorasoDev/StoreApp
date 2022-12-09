package uwu.victoraso.storeapp.ui.home.feed

import androidx.compose.foundation.clickable
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
import uwu.victoraso.storeapp.ui.home.DestinationBarWithLogo

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
            StoreAppSurface(modifier = modifier.fillMaxSize()) {
                Column {
                    DestinationBarWithLogo(
                        onDestinationBarButtonClick = onProductCreate,
                        onLogoButtonClick = {

                        }
                    )
                    FeedContent(
                        headerProductState = feedUiState.headerProduct,
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
    headerProductState: FeedHeaderUiState,
    productCollections: List<ProductCollection>,
    onProductClick: (Long, String) -> Unit,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyColumnState = rememberLazyListState()

    Box(modifier = modifier) {
        LazyColumn(state = lazyColumnState) {
            item {
                FeedProductHeader(headerProductState, onProductClick)
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
    headerProductState: FeedHeaderUiState,
    onProductClick: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (headerProductState) {
        is FeedHeaderUiState.Success -> {
            Box(modifier = modifier.clickable { onProductClick(headerProductState.product.id, headerProductState.product.category) }) {
                ProductImage(
                    imageUrl = headerProductState.product.imageUrl,
                    contentDescription = "${headerProductState.product.name} image",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                )
            }
        }
        is FeedHeaderUiState.Loading -> {}
        is FeedHeaderUiState.Error -> {}
    }
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

    return productCollections
}