package uwu.victoraso.storeapp.ui.home.feed

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uwu.victoraso.storeapp.model.Filter
import uwu.victoraso.storeapp.model.ProductCollection
import uwu.victoraso.storeapp.model.ProductRepo
import uwu.victoraso.storeapp.ui.components.FilterBar
import uwu.victoraso.storeapp.ui.components.ProductCollection
import uwu.victoraso.storeapp.ui.components.StoreAppDivider
import uwu.victoraso.storeapp.ui.components.StoreAppSurface
import uwu.victoraso.storeapp.ui.home.DestinationBar
import uwu.victoraso.storeapp.ui.home.FilterScreen
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun Feed(
    onProductClick: (Long, String) -> Unit,
    onProductList: (String) -> Unit,
    onProductCreate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel,
) {
    val feedUiState: FeedScreenUiState by viewModel.uiState.collectAsStateWithLifecycle()

    val productsCollections = getProductsCollections(feedUiState)
    val filters = remember { ProductRepo.getFilters() }
    Feed(
        feedUiState = feedUiState,
        productCollections = productsCollections,
        filters = filters,
        onProductClick = onProductClick,
        onProductList = onProductList,
        onProductCreate = onProductCreate,
        modifier = modifier,
    )
}

@Composable
private fun Feed(
    feedUiState: FeedScreenUiState,
    productCollections: List<ProductCollection>,
    filters: List<Filter>,
    onProductClick: (Long, String) -> Unit,
    onProductList: (String) -> Unit,
    onProductCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (feedUiState.processors) {
        FeedUiState.Loading -> {
            Log.d(DEBUG_TAG, "Feed Loading")
            StoreAppSurface(modifier = modifier.fillMaxSize()) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "Loading")
                }
            }
        }
        is FeedUiState.Success -> {
            Log.d(DEBUG_TAG, "Feed Success -> ${feedUiState.processors.productCollection.products.size}")
            StoreAppSurface(modifier = modifier.fillMaxSize()) {
                Box {
                    ProductCollectionList(
                        productCollections = productCollections,
                        filters = filters,
                        onProductClick = onProductClick,
                        onProductList = onProductList,
                    )
                    DestinationBar(onDestinationBarButtonClick = onProductCreate)
                }
            }
        }
        FeedUiState.Loading -> {
            Log.d(DEBUG_TAG, "Feed Error")
        }
        else -> {}
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
    filters: List<Filter>,
    onProductClick: (Long, String) -> Unit,
    onProductList: (String) -> Unit,
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
                FilterBar(filters, onShowFilter = { filtersVisible = true })
            }
            //TODO: quitar el productCollection y poner state.products. Habría que obtener todas las collecciones distintas si quiero seguir pasandole collecicones como objetos
            itemsIndexed(productCollections) { index, productCollection ->
                if (index > 0) {
                    StoreAppDivider(thickness = 2.dp)
                }

                ProductCollection(
                    productCollection = productCollection,
                    onProductClick = onProductClick,
                    onProductList = onProductList,
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