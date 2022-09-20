package uwu.victoraso.storeapp.ui.productcollection

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.ui.components.ProductImage
import uwu.victoraso.storeapp.ui.components.StoreAppDivider
import uwu.victoraso.storeapp.ui.components.StoreAppSurface
import uwu.victoraso.storeapp.ui.theme.Neutral8
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import uwu.victoraso.storeapp.ui.utils.formatPrice
import uwu.victoraso.storeapp.ui.utils.mirroringBackIcon

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ProductList(
    onProductSelected: (Long) -> Unit,
    category: String,
    upPress: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductListViewModel
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
                    Header(category)
                    Up(upPress = upPress)
                    ListContent(
                        productList = productListUiState.mainList,
                        onProductSelected = onProductSelected,
                        addProduct = addProduct,
                        removeProduct = removeProduct,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
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

@Composable //TODO juntar con el de ProductCreate con el parámetro título, para usarlo en distintos sitios
private fun Header(categoryName: String) {
    Spacer(
        modifier = Modifier
            .height(55.dp)
            .fillMaxWidth()
            .background(Brush.horizontalGradient(StoreAppTheme.colors.tornado1))
    )
    Text(
        text = categoryName,
        style = MaterialTheme.typography.subtitle1,
        fontSize = 18.sp,
        color = StoreAppTheme.colors.textInteractive,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .padding(top = 15.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = Neutral8.copy(alpha = 0.32f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = mirroringBackIcon(),
            tint = StoreAppTheme.colors.iconInteractive,
            contentDescription = null
        )
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
    LazyColumn(modifier = modifier) {
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
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onProductSelected(product.id) }
            .background(StoreAppTheme.colors.uiBackground)
            .padding(horizontal = 24.dp)
    ) {
        val (divider, image, name, tag, priceSpacer, price) = createRefs()
        createVerticalChain(name, tag, priceSpacer, price, chainStyle = ChainStyle.Packed)

        ProductImage(
            imageUrl = product.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                }
        )
        Text(
            text = product.name,
            style = MaterialTheme.typography.subtitle1,
            color = StoreAppTheme.colors.textSecondary,
            modifier = Modifier.constrainAs(name) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = parent.end,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        Text(
            text = product.tagline,
            style = MaterialTheme.typography.body1,
            color = StoreAppTheme.colors.textHelp,
            modifier = Modifier.constrainAs(tag) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    endMargin = 16.dp,
                    end = parent.end,
                    bias = 0f
                )
            }
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .constrainAs(priceSpacer) {
                    linkTo(top = tag.bottom, bottom = price.top)
                }
        )
        Text(
            text = formatPrice(product.price),
            style = MaterialTheme.typography.subtitle1,
            color = StoreAppTheme.colors.textPrimary,
            modifier = Modifier.constrainAs(price) {
                linkTo(
                    start = image.end,
                    end = parent.end,
                    startMargin = 16.dp,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        StoreAppDivider(
            Modifier.constrainAs(divider) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.bottom)
            }
        )
    }
}
