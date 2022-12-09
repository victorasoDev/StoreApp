package uwu.victoraso.storeapp.ui.productcollection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.StoreAppFilters
import uwu.victoraso.storeapp.ui.components.ProductImage
import uwu.victoraso.storeapp.ui.components.StoreAppDivider
import uwu.victoraso.storeapp.ui.components.StoreAppSurface
import uwu.victoraso.storeapp.ui.components.StoreAppTopBar
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.formatPrice
import java.lang.Math.random

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ProductList(
    onProductSelected: (Long) -> Unit,
    category: String,
    upPress: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val productListUiState: ProductListUiState by viewModel.productListUiState.collectAsStateWithLifecycle()

    ProductList(
        onProductSelected = onProductSelected,
        productListUiState = productListUiState,
        category = category,
        upPress = upPress,
        modifier = modifier
    )
}

@Composable
fun ProductList(
    onProductSelected: (Long) -> Unit,
    productListUiState: ProductListUiState,
    category: String,
    upPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    when(productListUiState) {
        is ProductListUiState.Success -> {
            StoreAppSurface(modifier = modifier.fillMaxSize()) {
                Box {
                    ListContent(
                        productList = productListUiState.mainList,
                        onProductSelected = onProductSelected,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                    StoreAppTopBar(upPress = upPress, screenTitle = category)
                }
            }
        }
        ProductListUiState.Loading -> {
        }
        ProductListUiState.Error -> {
        }
    }
}

@Composable
fun ListContent(
    productList: List<Product>,
    onProductSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val resources = LocalContext.current.resources
    val lazyColumnState = rememberLazyListState()
    LazyColumn(modifier = modifier, state = lazyColumnState) {
        item {
            Spacer(
                modifier = Modifier.windowInsetsTopHeight(
                    WindowInsets.statusBars.add(WindowInsets(top = 56.dp))
                )
            )
        }
        items(productList) { product ->
            ProductListItem(
                product = product,
                onProductSelected = onProductSelected
            )
        }
    }
}


@Composable
fun ProductListItem(
    product: Product,
    onProductSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable { onProductSelected(product.id) }
    ) {
        ProductListItemContent(product = product)
    }
}

@Composable
fun ProductListItem(
    product: Product,
    onProductSelected: (Long, String) -> Unit,
    small: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable { onProductSelected(product.id, product.category.ifEmpty { StoreAppFilters[random().toInt()].name }) }
    ) {
        if (small) {
            ProductListItemSmallContent(product = product)
        } else {
            ProductListItemContent(product = product)
        }
    }
}

@Composable
private fun ProductListItemContent(
    product: Product
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .padding(start = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProductImage(
            imageUrl = product.iconUrl,
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.subtitle1,
                color = StoreAppTheme.colors.textSecondary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth()
            )
            Text(
                text = formatPrice(product.price),
                style = MaterialTheme.typography.subtitle1,
                color = StoreAppTheme.colors.textPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
    StoreAppDivider(modifier = Modifier.fillMaxWidth())
}

@Composable
private fun ProductListItemSmallContent(
    product: Product,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(vertical = 8.dp)
            .padding(start = 24.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProductImage(
            imageUrl = product.iconUrl,
            contentDescription = null,
            modifier = Modifier.size(45.dp)
        )
        Column {
            Text(
                text = product.name,
                style = MaterialTheme.typography.subtitle2,
                color = StoreAppTheme.colors.textSecondary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth()
            )
            Text(
                text = formatPrice(product.price),
                style = MaterialTheme.typography.subtitle2,
                color = StoreAppTheme.colors.textPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            )
        }
    }
    StoreAppDivider(modifier = Modifier.fillMaxWidth())
}
