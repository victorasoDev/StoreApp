package uwu.victoraso.storeapp.ui.productcollection

import android.util.Log
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
import uwu.victoraso.storeapp.ui.components.ProductImage
import uwu.victoraso.storeapp.ui.components.StoreAppDivider
import uwu.victoraso.storeapp.ui.components.StoreAppSurface
import uwu.victoraso.storeapp.ui.components.StoreAppTopBar
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import uwu.victoraso.storeapp.ui.utils.formatPrice

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
        addProduct = viewModel::addProductToCart,
        removeProduct = viewModel::removeProductFromCart,
        modifier = modifier
    )
}

@Composable
fun ProductList(
    onProductSelected: (Long) -> Unit,
    productListUiState: ProductListUiState,
    category: String,
    upPress: () -> Unit,
    addProduct: (Long) -> Unit,
    removeProduct: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when(productListUiState) {
        is ProductListUiState.Success -> {
            Log.d(DEBUG_TAG, " - " + productListUiState.mainList.size.toString()) //TODO
            StoreAppSurface(modifier = modifier.fillMaxSize()) {
                Box {
                    ListContent(
                        productList = productListUiState.mainList,
                        onProductSelected = onProductSelected,
                        addProduct = addProduct,
                        removeProduct = removeProduct,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                    StoreAppTopBar(upPress = upPress, screenTitle = category)
                }
            }
        }
        ProductListUiState.Loading -> {
            Log.d(DEBUG_TAG, " - Loading") //TODO
        }
        ProductListUiState.Error -> {
            Log.d(DEBUG_TAG, " - Error") //TODO
        }
    }
}

@Composable
fun ListContent(
    productList: List<Product>,
    onProductSelected: (Long) -> Unit,
    addProduct: (Long) -> Unit,
    removeProduct: (Long) -> Unit,
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
            //TODO: el filtro habrá que meterlo por aqui
        }
        items(productList) { product ->
            //TODO: posible swipe para añadir (en cart está para eliminar)
            ProductListItem(
                product = product,
                onProductSelected = onProductSelected,
                addProduct = addProduct,
                removeProduct = removeProduct
            )
        }
    }
}


@Composable
fun ProductListItem(
    product: Product,
    onProductSelected: (Long) -> Unit,
    addProduct: (Long) -> Unit,
    removeProduct: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable { onProductSelected(product.id) }
    ) {
        ProductListItemContent(
            product = product,
            addProduct = addProduct,
            removeProduct = removeProduct,
        )
    }
}

@Composable
fun ProductListItem(
    product: Product,
    onProductSelected: (Long, String) -> Unit,
    addProduct: (Long) -> Unit,
    removeProduct: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable { onProductSelected(product.id, product.category) }
    ) {
        ProductListItemContent(
            product = product,
            addProduct = addProduct,
            removeProduct = removeProduct
        )
    }
}

@Composable
private fun ProductListItemContent(
    product: Product,
    addProduct: (Long) -> Unit,
    removeProduct: (Long) -> Unit,
    modifier: Modifier = Modifier
) { //TODO

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
