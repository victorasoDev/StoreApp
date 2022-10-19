package uwu.victoraso.storeapp.ui.productdetail

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.Cart
import uwu.victoraso.storeapp.model.CartProduct
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.fill
import uwu.victoraso.storeapp.ui.components.*
import uwu.victoraso.storeapp.ui.home.HomeSections
import uwu.victoraso.storeapp.ui.theme.Neutral8
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.formatPrice
import uwu.victoraso.storeapp.ui.utils.mirroringBackIcon

private val BottomBarHeight = 56.dp
private val TitleHeight = 128.dp
private val GradientScroll = 120.dp
private val ImageOverlap = 70.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 60.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 200.dp
private val CollapsedImageSize = 75.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ProductDetail(
    upPress: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel(),
    onProductClick: (Long, String) -> Unit,
    onNavigateTo: (String) -> Unit
) {
    val productDetailScreenUiState: ProductDetailScreenUiState by viewModel.uiState.collectAsStateWithLifecycle()

    val productDetailState = productDetailScreenUiState.product
    val relatedState = productDetailScreenUiState.relatedProducts
    val cartsState = productDetailScreenUiState.carts

    ProductDetail(
        upPress = upPress,
        productDetailState = productDetailState,
        relatedState = relatedState,
        cartsState = cartsState,
        onProductClick = onProductClick,
        onWishlistClick = viewModel::wishlistItemToggle,
        onAddToCartClick = viewModel::addToCart,
        onAddCartClick = viewModel::addCart,
        onNavigateTo = onNavigateTo
    )
}

@Composable
private fun ProductDetail(
    upPress: () -> Unit,
    productDetailState: ProductDetailUiState,
    relatedState: RelatedProductsUiState,
    cartsState: CartsUiState,
    onProductClick: (Long, String) -> Unit,
    onWishlistClick: (Long, Boolean) -> Unit,
    onAddToCartClick: (CartProduct) -> Unit,
    onAddCartClick: (Cart) -> Unit,
    onNavigateTo: (String) -> Unit
) {
    var titleLines by remember { mutableStateOf(1) }
    when (productDetailState) {
        is ProductDetailUiState.Success -> {
            val product = productDetailState.product
            Box(modifier = Modifier.fillMaxSize()) {
                val scroll = rememberScrollState(0)
                Header()
                Body(
                    relatedState = relatedState,
                    product = product,
                    onNavigateTo = onNavigateTo,
                    onProductClick = onProductClick,
                    titleLines = titleLines,
                    scroll = scroll
                ) // TODO: el body espera un par de colecciones
                Title(
                    product = product,
                    onWishlistClick = onWishlistClick,
                    isWishlistItem = product.isWishlist,
                    countTitleLines = { lines -> titleLines = lines }
                ) { scroll.value }
                Image(
                    imageUrl = product.imageUrl
                ) { scroll.value }
                Up(upPress)
                CartBottomBar(
                    product = product,
                    cartsState = cartsState,
                    onAddToCartClick = onAddToCartClick,
                    onAddCartClick = onAddCartClick,
                    onNavigateTo = onNavigateTo,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .imePadding()
                )
            }

        }
        is ProductDetailUiState.Loading -> {
            StoreAppCircularIndicator()
        }
        is ProductDetailUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error")
            }
        }
    }
}

@Composable
private fun Header() {
    Spacer(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(Brush.horizontalGradient(StoreAppTheme.colors.tornado1))
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
private fun Body(
//    related: List<ProductCollection>,
    relatedState: RelatedProductsUiState,
    product: Product,
    onNavigateTo: (String) -> Unit,
    onProductClick: (Long, String) -> Unit,
    titleLines: Int,
    scroll: ScrollState
) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTitleOffset)
        )
        Column(
            modifier = Modifier.verticalScroll(scroll)
        ) {
            Spacer(modifier = Modifier.height(GradientScroll))
            StoreAppSurface(Modifier.fillMaxWidth()) {
                Column {
                    Spacer(modifier = Modifier.height(ImageOverlap))
                    Spacer(modifier = Modifier.height(TitleHeight))
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.detail_header, product.name),
                        style = MaterialTheme.typography.subtitle1,
                        color = StoreAppTheme.colors.textHelp,
                        modifier = if (titleLines > 1) HzPadding.padding(top = 20.dp) else HzPadding
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    var seeMore by remember { mutableStateOf(true) }
                    Text(
                        text = stringResource(id = R.string.detail_placeholder),
                        style = MaterialTheme.typography.body1,
                        color = StoreAppTheme.colors.textHelp,
                        maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis,
                        modifier = HzPadding
                    )
                    val textButton = if (seeMore) {
                        stringResource(id = R.string.see_more)
                    } else {
                        stringResource(id = R.string.see_less)
                    }
                    Text(
                        text = textButton,
                        style = MaterialTheme.typography.button,
                        textAlign = TextAlign.Center,
                        color = StoreAppTheme.colors.textLink,
                        modifier = Modifier
                            .heightIn(20.dp)
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                            .clickable {
                                seeMore = !seeMore
                            }
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        text = stringResource(id = R.string.ingredients),
                        style = MaterialTheme.typography.body1,
                        color = StoreAppTheme.colors.textHelp,
                        modifier = HzPadding
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.ingredients_list),
                        style = MaterialTheme.typography.body1,
                        color = StoreAppTheme.colors.textHelp,
                        modifier = HzPadding
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    StoreAppDivider()

                    when (relatedState) {
                        is RelatedProductsUiState.Success -> {
                            // eliminar el producto de la ficha del listado de relacionados
                            relatedState.productCollection.products =
                                relatedState.productCollection.products.filter { p -> p.id != product.id }
                            ProductCollection(
                                productCollection = relatedState.productCollection,
                                onNavigateTo = onNavigateTo,
                                onProductClick = onProductClick,
                                highlight = false
                            )
                        }
                        is RelatedProductsUiState.Loading -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                StoreAppCircularIndicator()
                            }
                        }
                        is RelatedProductsUiState.Error -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                //TODO:
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier
                            .padding(bottom = BottomBarHeight)
                            .navigationBarsPadding()
                            .height(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Title(
    product: Product,
    onWishlistClick: (Long, Boolean) -> Unit,
    isWishlistItem: Boolean,
    countTitleLines: (Int) -> Unit,
    scrollProvider: () -> Int
) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }

    var isWishlist by remember { mutableStateOf(isWishlistItem) }

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .statusBarsPadding()
            .graphicsLayer { //https://medium.com/androiddevelopers/jetpack-compose-debugging-recomposition-bfcf4a6f8d37
                val offset = (maxOffset - scrollProvider()).coerceAtLeast(minOffset)
                translationY = offset
            }
            .background(color = StoreAppTheme.colors.uiBackground)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = product.name,
            style = MaterialTheme.typography.h5,
            color = StoreAppTheme.colors.textSecondary,
            modifier = HzPadding.then(Modifier.padding(end = CollapsedImageSize)),
            onTextLayout = { textLayoutResult -> countTitleLines(textLayoutResult.lineCount) }
        )
        Text(
            text = product.tagline,
            style = MaterialTheme.typography.subtitle2,
            fontSize = 20.sp,
            color = StoreAppTheme.colors.textHelp,
            modifier = HzPadding
        )
        Spacer(Modifier.height(4.dp))
        Row(modifier = HzPadding, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = formatPrice(product.price),
                style = MaterialTheme.typography.h6,
                color = StoreAppTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.width(8.dp))
            WishlistToggleButton(
                isWishlistItem = isWishlist,
                onClick = {
                    isWishlist = !isWishlist
                    onWishlistClick(product.id, isWishlist)
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        StoreAppDivider()
    }
}

@Composable
private fun WishlistToggleButton(
    isWishlistItem: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    StoreAppToggleButton(
        checked = isWishlistItem,
        onCheckedChange = { onClick() },
        modifier = modifier,
        icon = {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                tint = StoreAppTheme.colors.brand,
                contentDescription = null
            )
        },
        checkedIcon = {
            Icon(
                imageVector = Icons.Outlined.Favorite,
                tint = StoreAppTheme.colors.brand,
                contentDescription = null
            )
        }
    )
}

@Composable
private fun Image(
    imageUrl: String,
    scrollProvider: () -> Int
) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFractionProvider = { (scrollProvider() / collapseRange).coerceIn(0f, 1f) }

    CollapsingImageLayout(
        collapseFractionProvider = collapseFractionProvider,
        modifier = HzPadding.then(Modifier.statusBarsPadding())
    ) {
        ProductImage(imageUrl = imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize())
    }

}

@Composable
private fun CollapsingImageLayout(
    collapseFractionProvider: () -> Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)

        val collapseFraction = collapseFractionProvider()

        val imageMaxSize = kotlin.math.min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = kotlin.math.max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / 2, // centered when expanded
            constraints.maxWidth - imageWidth, // right aligned when collapsed
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }
}

@Composable
private fun CartBottomBar(
    product: Product,
    cartsState: CartsUiState,
    modifier: Modifier = Modifier,
    onAddToCartClick: (CartProduct) -> Unit,
    onAddCartClick: (Cart) -> Unit,
    onNavigateTo: (String) -> Unit
) {
    var dropdownMenuExpanded by remember { mutableStateOf(false) }
    when (cartsState) {
        is CartsUiState.Success -> {
            StoreAppSurface(modifier = modifier) {
                Column {
                    StoreAppDivider()
                    StoreAppDropdownMenu(
                        expanded = dropdownMenuExpanded,
                        onDismissRequest = { dropdownMenuExpanded = false },
                        items = cartsState.carts,
                        onItemClick = { cart ->
                            onAddToCartClick(
                                CartProduct().fill(product, cart)
                            )
                        },
                        onAddCartClick = { cartName ->
                            onAddCartClick(Cart(name = cartName))
                        },
                        itemText = { item ->
                            Text(item.name, color = StoreAppTheme.colors.textLink)
                        },
                        modifier = Modifier.navigationBarsPadding()
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .navigationBarsPadding()
                            .then(HzPadding)
                            .heightIn(min = BottomBarHeight)
                    ) {
                        StoreAppButton(
                            onClick = { dropdownMenuExpanded = !dropdownMenuExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.add_to_cart))
                            Text(
                                text = stringResource(R.string.add_to_cart),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        StoreAppButton(
                            onClick = { onNavigateTo(HomeSections.CART.route) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = stringResource(id = R.string.go_to_cart))
                            Text(
                                text = stringResource(R.string.go_to_cart),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }
                    }

                }
            }
        }
        is CartsUiState.Error -> {
            //TODO
        }
        is CartsUiState.Loading -> {} //TODO eliminar
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun SnackDetailPreview() {
    StoreAppTheme {
        StoreAppSurface {
            Row(modifier = HzPadding, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = formatPrice(434314),
                    style = MaterialTheme.typography.h6,
                    color = StoreAppTheme.colors.textPrimary,
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        tint = StoreAppTheme.colors.brand,
                        contentDescription = null
                    )
                }
            }
        }
    }
}