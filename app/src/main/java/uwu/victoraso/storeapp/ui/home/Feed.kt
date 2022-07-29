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
    modifier: Modifier = Modifier
) {
    val productsCollection = remember { ProductRepo.getProducts() }
    val filters = remember { ProductRepo.getFilters() }
    Feed(
        productsCollection,
        filters,
        onProductClick,
        modifier
    )
}

@Composable
private fun Feed(
    productCollections: List<ProductCollection>,
    filters: List<Filter>,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    StoreAppSurface(modifier = modifier.fillMaxSize()) {
        Box {
            ProductCollectionList(productCollections = productCollections, filters = filters, onProductClick = onProductClick)
            DestinationBar()
        }
    }
}

@Composable
private fun ProductCollectionList(
    productCollections: List<ProductCollection>,
    filters: List<Filter>,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier
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
//        FilterScreen()
    }
}