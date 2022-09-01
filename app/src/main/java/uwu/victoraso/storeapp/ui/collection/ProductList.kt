package uwu.victoraso.storeapp.ui.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavBackStackEntry
import uwu.victoraso.storeapp.model.OrderLine
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.ProductRepo
import uwu.victoraso.storeapp.ui.components.ProductImage
import uwu.victoraso.storeapp.ui.components.StoreAppDivider
import uwu.victoraso.storeapp.ui.components.StoreAppSurface
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.formatPrice

@Composable
fun ProductList(
    onProductSelected: (Long, NavBackStackEntry) -> Unit,
    upPress: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductListViewModel
) {
    ProductList(
        onProductSelected = onProductSelected,
        addProduct = viewModel::addProductToCart,
        removeProduct = viewModel::removeProductFromCart,
        modifier = modifier
    )
}

@Composable
fun ProductList(
    onProductSelected: (Long, NavBackStackEntry) -> Unit,
    addProduct: (Long) -> Unit,
    removeProduct: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val productList = ProductRepo.getInspiredByCart().products
    StoreAppSurface(modifier = modifier.fillMaxSize()) {
        Box {
            ListContent(
                productList = productList,
                onProductSelected = onProductSelected,
                addProduct = addProduct,
                removeProduct = removeProduct,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun ListContent(
    productList: List<Product>,
    onProductSelected: (Long, NavBackStackEntry) -> Unit,
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
    onProductSelected: (Long, NavBackStackEntry) -> Unit,
    addProduct: (Long) -> Unit,
    removeProduct: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { /*onProductSelected()*/ }
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
