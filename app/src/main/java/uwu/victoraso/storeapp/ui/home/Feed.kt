package uwu.victoraso.storeapp.ui.home

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import uwu.victoraso.storeapp.model.Filter
import uwu.victoraso.storeapp.model.ProductCollection
import uwu.victoraso.storeapp.model.ProductRepo
import uwu.victoraso.storeapp.ui.components.FilterBar
import uwu.victoraso.storeapp.ui.components.ProductCollection
import uwu.victoraso.storeapp.ui.components.StoreAppDivider
import uwu.victoraso.storeapp.ui.components.StoreAppSurface

@Composable
fun Feed(
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    refreshData: () -> Unit,
    state: ProductListState
) {
    val productsCollection = remember { ProductRepo.getProducts() }
    val filters = remember { ProductRepo.getFilters() }
    Feed(
        productCollections = productsCollection,
        filters = filters,
        onProductClick = onProductClick,
        modifier = modifier,
        isRefreshing = isRefreshing,
        refreshData = refreshData,
        state = state
    )
}

@Composable
private fun Feed(
    productCollections: List<ProductCollection>,
    filters: List<Filter>,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
    refreshData: () -> Unit,
    state: ProductListState
) {
    StoreAppSurface(modifier = modifier.fillMaxSize()) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = refreshData
        ) {
            Box {
                ProductCollectionList(productCollections = productCollections, filters = filters, onProductClick = onProductClick, state = state)
                DestinationBar()
            }
        }
    }
}

@Composable
private fun ProductCollectionList(
    productCollections: List<ProductCollection>,
    filters: List<Filter>,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    state: ProductListState
) {
    var filtersVisible by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier) {
        LazyColumn {
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