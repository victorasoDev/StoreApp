package uwu.victoraso.storeapp.ui.home.cart

import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.behavior.SwipeDismissBehavior
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.CartProduct
import uwu.victoraso.storeapp.model.OrderLine
import uwu.victoraso.storeapp.model.ProductCollection
import uwu.victoraso.storeapp.model.ProductRepo
import uwu.victoraso.storeapp.ui.components.*
import uwu.victoraso.storeapp.ui.home.DestinationBar
import uwu.victoraso.storeapp.ui.home.HomeSections
import uwu.victoraso.storeapp.ui.theme.AlphaNearOpaque
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.formatPrice

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun Cart(
    onProductClick: (Long, String) -> Unit,
    onProductList: (String) -> Unit,
    modifier: Modifier = Modifier,
    realViewModel: RealCartViewModel = hiltViewModel()
) {

    val cartUiState: CartScreenUiState by realViewModel.uiState.collectAsStateWithLifecycle()

    val inspiredByCart: InspiredByCartProductsUiState = cartUiState.inspiredByCartProductsUiState
    val cart: CartProductsUiState = cartUiState.cartProductsUiState

    Cart(
        removeProduct = realViewModel::removeProduct,
        inspiredByCart = inspiredByCart,
        cart = cart,
        onProductClick = onProductClick,
        onProductList = onProductList,
        modifier = modifier
    )
}

@Composable
fun Cart(
    removeProduct: (Long, Long) -> Unit,
    inspiredByCart: InspiredByCartProductsUiState,
    cart: CartProductsUiState,
    onProductClick: (Long, String) -> Unit,
    onProductList: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    StoreAppSurface(modifier = modifier.fillMaxSize()) {
        Box {
            CartContent(
                removeProduct = removeProduct,
                inspiredByCart = inspiredByCart,
                cart = cart,
                onProductClick = onProductClick,
                onProductList = onProductList,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            DestinationBar(
                modifier = Modifier.align(Alignment.TopCenter),
                title = "Your shopping cart",
                imageVector = Icons.Default.ShoppingCartCheckout,
                onDestinationBarButtonClick = { }
            )
        }
    }
}

@Composable
fun CartContent(
    removeProduct: (Long, Long) -> Unit,
    inspiredByCart: InspiredByCartProductsUiState,
    cart: CartProductsUiState,
    onProductClick: (Long, String) -> Unit,
    onProductList: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val resources = LocalContext.current.resources
    var productCount by remember { mutableStateOf(0) }
    var totalCost by remember { mutableStateOf(0L) }
    val productCountFormattedString = remember(0, resources) {
        resources.getQuantityString(
            R.plurals.cart_order_count,
            productCount, productCount
        )
    }

    LazyColumn(modifier) {
        item {
            Spacer(
                Modifier.windowInsetsTopHeight(
                    WindowInsets.statusBars.add(WindowInsets(top = 56.dp))
                )
            )
            Text(
                text = stringResource(id = R.string.cart_order_header, productCountFormattedString),
                style = MaterialTheme.typography.h6,
                color = StoreAppTheme.colors.brand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .heightIn(min = 56.dp)
                    .padding(horizontal = 24.dp, vertical = 4.dp)
                    .wrapContentHeight()
            )
        }
        when (cart) {
            is CartProductsUiState.Success -> {
                productCount = cart.cart.itemCount
                totalCost = cart.cart.cartItems.sumOf { it.price }
                items(cart.cart.cartItems) { cartProduct ->
                    SwipeDismissBehavior {
                        CartItem(
                            cartProduct = cartProduct,
                            removeProduct = removeProduct,
                            onProductClick = onProductClick
                        )
                    }
                }
            }
            is CartProductsUiState.Loading -> {
                //TODO
            }
            is CartProductsUiState.Error -> {
                //TODO
            }
        }

        item {
            SummaryItem(
                subtotal = totalCost,
                shippingCosts = 369
            )
        }
        /** Check flow status before print the collection **/
        if (inspiredByCart is InspiredByCartProductsUiState.Success) {
            item {
                ProductCollection(
                    productCollection = inspiredByCart.productCollection,
                    onProductClick = onProductClick,
                    onProductList = onProductList,
                    highlight = true,
                    showMore = false
                )
                Spacer(modifier = Modifier.height(56.dp))
            }
        }
    }
}

@Composable
fun CartItem(
    cartProduct: CartProduct,
    removeProduct: (Long, Long) -> Unit,
    onProductClick: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onProductClick(cartProduct.id, cartProduct.category) }
            .background(StoreAppTheme.colors.uiBackground)
            .padding(horizontal = 24.dp)
    ) {
        val (divider, image, name, tag, priceSpacer, price, remove, quantity) = createRefs()
        createVerticalChain(name, tag, priceSpacer, price, chainStyle = ChainStyle.Packed)
        ProductImage(
            imageUrl = cartProduct.imageUrl,
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
            text = cartProduct.name,
            style = MaterialTheme.typography.subtitle1,
            color = StoreAppTheme.colors.textSecondary,
            modifier = Modifier.constrainAs(name) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = remove.start,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        IconButton(
            onClick = { removeProduct(cartProduct.id, cartProduct.cartId) },
            modifier = Modifier
                .constrainAs(remove) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .padding(top = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = StoreAppTheme.colors.iconSecondary
            )
        }
        Text(
            text = "A tagline",
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
            text = formatPrice(cartProduct.price),
            style = MaterialTheme.typography.subtitle1,
            color = StoreAppTheme.colors.textPrimary,
            modifier = Modifier.constrainAs(price) {
                linkTo(
                    start = image.end,
                    end = quantity.start,
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

@Composable
fun SummaryItem(
    subtotal: Long,
    shippingCosts: Long,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = stringResource(id = R.string.cart_summary_header),
            style = MaterialTheme.typography.h6,
            color = StoreAppTheme.colors.brand,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .heightIn(min = 56.dp)
                .wrapContentHeight()
        )
        Row(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = stringResource(id = R.string.cart_subtotal_label),
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .alignBy(LastBaseline)
            )
            Text(
                text = formatPrice(subtotal),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }
        Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
            Text(
                text = stringResource(id = R.string.cart_shipping_label),
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .alignBy(LastBaseline)
            )
            Text(
                text = formatPrice(shippingCosts),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        StoreAppDivider()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            StoreAppButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCartCheckout,
                    tint = StoreAppTheme.colors.brand,
                    contentDescription = null
                )
                Text(
                    text = formatPrice(subtotal + shippingCosts),
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .alignBy(LastBaseline)
                        .padding(start = 8.dp)
                )

            }
        }

        StoreAppDivider()
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun CartPreview() {
    StoreAppTheme {
        //TODO:
    }
}