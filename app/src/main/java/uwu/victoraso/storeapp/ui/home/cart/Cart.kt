package uwu.victoraso.storeapp.ui.home.cart

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.Cart
import uwu.victoraso.storeapp.model.CartProduct
import uwu.victoraso.storeapp.ui.components.*
import uwu.victoraso.storeapp.ui.home.DestinationBar
import uwu.victoraso.storeapp.ui.home.cart.payment.PaymentDialog
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG_LOGIN
import uwu.victoraso.storeapp.ui.utils.formatPrice

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun Cart(
    onProductClick: (Long, String) -> Unit,
    onProductList: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = hiltViewModel()
) {

    val cartUiState: CartScreenUiState by viewModel.uiState.collectAsStateWithLifecycle()

    val inspiredByCart: InspiredByCartProductsUiState = cartUiState.inspiredByCartProductsUiState
    val cart: CartsProductsUiState = cartUiState.cartProductsUiState
    val selectedCart: SelectedCartUiState = cartUiState.selectedCartUiState

    Cart(
        removeProduct = viewModel::removeProduct,
        changeCartName = viewModel::changeCartName,
        insertCart = viewModel::insertCart,
        setSelectedCartIndex = viewModel::setSelectedCartIndex,
        inspiredByCart = inspiredByCart,
        cartUiState = cart,
        selectedCartUiState = selectedCart,
        onProductClick = onProductClick,
        onProductList = onProductList,
        modifier = modifier
    )
}

@Composable
fun Cart(
    removeProduct: (Long, Long) -> Unit,
    changeCartName: (Cart) -> Unit,
    insertCart: (Cart) -> Unit,
    setSelectedCartIndex: (Int) -> Unit,
    inspiredByCart: InspiredByCartProductsUiState,
    cartUiState: CartsProductsUiState,
    selectedCartUiState: SelectedCartUiState,
    onProductClick: (Long, String) -> Unit,
    onProductList: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCart by remember { mutableStateOf(0) }
    var dropdownMenuExpanded by remember { mutableStateOf(false) }
    var nameTextFieldValue by remember { mutableStateOf("") }

    when (cartUiState) {
        is CartsProductsUiState.Success -> {
            if (selectedCartUiState is SelectedCartUiState.Success) selectedCart = selectedCartUiState.selectedCartIndex
            val mainCart: Cart
            if (cartUiState.carts.isNotEmpty()) {
                mainCart = cartUiState.carts[selectedCart]
            } else {
                mainCart = Cart(name = "New cart")
                insertCart(mainCart)
            }
            nameTextFieldValue = mainCart.name
            StoreAppSurface(modifier = modifier.fillMaxSize()) {
                Box {
                    CartContent(
                        removeProduct = removeProduct,
                        inspiredByCart = inspiredByCart,
                        cart = mainCart,
                        onProductClick = onProductClick,
                        onProductList = onProductList,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                    Column(modifier = Modifier.align(Alignment.TopCenter)) {
                        DestinationBar(
                            title = nameTextFieldValue,
                            onTitleValueChange = { nameTextFieldValue = it },
                            imageVector = Icons.Outlined.ExpandMore,
                            onDestinationBarButtonClick = { dropdownMenuExpanded = !dropdownMenuExpanded },
                            onChangeCartNameClick = { changeCartName(mainCart.copy(name = it)) }
                        )
                        Box(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp, top = 8.dp),
                            contentAlignment = TopEnd
                        ) {
                            StoreAppDropdownMenu(
                                expanded = dropdownMenuExpanded,
                                onDismissRequest = { dropdownMenuExpanded = false },
                                items = cartUiState.carts,
                                onItemClick = { cart ->
                                    selectedCart = cartUiState.carts.indexOf(cart)
                                    setSelectedCartIndex(selectedCart)
                                },
                                onAddCartClick = { },
                                itemText = { item ->
                                    Text(item.name, color = StoreAppTheme.colors.textLink)
                                },
                            )
                        }
                    }
                }
            }
        }
        is CartsProductsUiState.Loading -> {
            //TODO
        }
        is CartsProductsUiState.Error -> {
            //TODO
        }
    }
}

@Composable
fun CartContent(
    removeProduct: (Long, Long) -> Unit,
    inspiredByCart: InspiredByCartProductsUiState,
    cart: Cart,
    onProductClick: (Long, String) -> Unit,
    onProductList: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d(DEBUG_TAG_LOGIN, "recomposes CartContent -> $cart")
    val resources = LocalContext.current.resources
    val productCount = cart.cartItems.size
    var totalCost by remember { mutableStateOf(0L) }
    val productCountFormattedString = resources.getQuantityString(R.plurals.cart_order_count, productCount, productCount)

    var isDialogShowing by remember { mutableStateOf(false) }
    PaymentDialog(show = isDialogShowing, cart = cart, onDismiss = { isDialogShowing = !isDialogShowing })

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
        totalCost = cart.cartItems.sumOf { it.price }
        items(cart.cartItems) { cartProduct ->
            SwipeDismissBehavior {
                CartItem(
                    cartProduct = cartProduct,
                    removeProduct = removeProduct,
                    onProductClick = onProductClick
                )
            }
        }

        item {
            SummaryItem(
                subtotal = totalCost,
                productsCount = productCount,
                showPaymentDialog = { isDialogShowing = true },
                shippingCosts = 369
            )
        }
        /** Check flow status before print the collection **/
        if (inspiredByCart is InspiredByCartProductsUiState.Success) {
            item {
                ProductCollection(
                    productCollection = inspiredByCart.productCollection,
                    onProductClick = onProductClick,
                    onNavigateTo = onProductList,
                    highlight = true,
                    showMore = false
                )
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
    Box(
        modifier = modifier
            .clickable { onProductClick(cartProduct.productId, cartProduct.category) }
            .fillMaxWidth()
            .background(StoreAppTheme.colors.uiBackground)
    ) {
        Row(
            modifier = Modifier.padding(all = 16.dp),
            verticalAlignment = CenterVertically
        ) {
            ProductImage(
                imageUrl = cartProduct.iconUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
            )
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = cartProduct.name,
                    style = MaterialTheme.typography.subtitle1,
                    color = StoreAppTheme.colors.textSecondary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 16.dp)

                )
                Text(
                    text = formatPrice(cartProduct.price),
                    style = MaterialTheme.typography.subtitle1,
                    color = StoreAppTheme.colors.textPrimary,
                    modifier = Modifier.padding(start = 8.dp, end = 16.dp)
                )
            }
        }
        IconButton(
            onClick = { removeProduct(cartProduct.productId, cartProduct.cartId) },
            modifier = modifier
                .align(TopEnd)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = StoreAppTheme.colors.iconSecondary
            )
        }
        StoreAppDivider(modifier = modifier.align(BottomCenter))
    }
}

@Composable
fun SummaryItem(
    subtotal: Long,
    productsCount: Int,
    showPaymentDialog: () -> Unit,
    shippingCosts: Long,
    modifier: Modifier = Modifier
) {

    Log.d(DEBUG_TAG_LOGIN, "recomposes SummaryItem -> $productsCount")
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
                onClick = { showPaymentDialog() },
                enabled = productsCount > 0
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCartCheckout,
                    tint = if (productsCount > 0) StoreAppTheme.colors.textInteractive else StoreAppTheme.colors.textInteractive.copy(alpha = 0.2f),
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
        CartItem(
            cartProduct = CartProduct(name = "Gygabyte GeForce RTX 3080", price = 1234323),
            removeProduct = { a, b -> },
            onProductClick = { a, b -> })
    }
}