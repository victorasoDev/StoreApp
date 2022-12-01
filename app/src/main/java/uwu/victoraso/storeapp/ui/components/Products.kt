package uwu.victoraso.storeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.ReadMore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import uwu.victoraso.storeapp.MainDestinations
import uwu.victoraso.storeapp.model.CollectionType
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.ProductCollection
import uwu.victoraso.storeapp.model.products
import uwu.victoraso.storeapp.ui.theme.AlphaNearOpaque
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.mirroringIcon

private val gradientWidth
    @Composable
    get() = with(LocalDensity.current) {
        (3 * (HighlightCardWidth + HighlightCardPadding).toPx())
    }

@Composable
fun ProductCollection(
    productCollection: ProductCollection,
    onProductClick: (Long, String) -> Unit,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
    index: Int = 0,
    highlight: Boolean = true,
    showMore: Boolean = true // showMore -> if true lastItem and arrow is visible
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(start = 24.dp)
        ) {
            Text(
                text = productCollection.name,
                style = MaterialTheme.typography.h6,
                color = StoreAppTheme.colors.brand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )
            if (showMore) {
                IconButton(
                    onClick = { onNavigateTo(MainDestinations.PRODUCT_LIST_ROUTE + "/"  + productCollection.name) },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = mirroringIcon(
                            ltrIcon = Icons.Outlined.ArrowForward,
                            rtlIcon = Icons.Outlined.ArrowBack
                        ),
                        tint = StoreAppTheme.colors.brand,
                        contentDescription = null
                    )
                }
            }
        }
        if (highlight && productCollection.type == CollectionType.Highlight) {
            HighlightedProducts(
                index = index,
                products = productCollection.products,
                onProductClick = onProductClick,
                onProductList = onNavigateTo,
                showMore = showMore
            )
        } else {
            Products(
                products = productCollection.products,
                onProductClick = onProductClick,
                onProductList = onNavigateTo
            )
        }
    }
}

private val HighlightCardWidth = 170.dp
private val HighlightCardPadding = 16.dp

@Composable
private fun HighlightedProducts(
    index: Int,
    products: List<Product>,
    onProductClick: (Long, String) -> Unit,
    onProductList: (String) -> Unit,
    modifier: Modifier = Modifier,
    showMore: Boolean
    ) {
    val scroll = rememberScrollState(0)
    val gradient = when ((index / 2) % 2) {
        0 -> StoreAppTheme.colors.gradientLavander
        else -> StoreAppTheme.colors.gradientDarkLavander
    }

    // The Cards show a gradient which spans 3 cards and scrolls with parallax.
    val gradientWidth = with(LocalDensity.current) {
        (6 * (HighlightCardWidth + HighlightCardPadding).toPx())
    }
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp)
    ) {
        itemsIndexed(products) { index, product ->
            if (products.lastIndex != index) {
                HighlightedProductItem(
                    product = product,
                    onProductClick = onProductClick,
                    index = index,
                    gradient = gradient,
                    gradientWidth = gradientWidth,
                    scroll = scroll.value
                )
            } else {
                HighlightedProductItem(
                    product = product,
                    onProductClick = onProductClick,
                    index = index,
                    gradient = gradient,
                    gradientWidth = gradientWidth,
                    scroll = scroll.value
                )
                if (showMore) {
                    ViewMoreHighlightCard(
                        modifier = modifier.padding(start = 16.dp),
                        onProductList = onProductList,
                        productCollectionCategory = products.first().category
                    )
                }
            }
        }
    }
}

@Composable
private fun Products(
    products: List<Product>,
    onProductClick: (Long, String) -> Unit,
    onProductList: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
    ) {
        itemsIndexed(products) { index, product ->
            if (products.lastIndex != index) {
                ProductItem(product = product, onProductClick = onProductClick)
            } else {
                ProductItem(product = product, onProductClick = onProductClick)
                ViewMoreCard(
                    modifier = modifier.padding(start = 16.dp),
                    onProductList = onProductList,
                    productCollectionCategory = products.first().category
                )
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onProductClick: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    StoreAppSurface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .padding(
                start = 4.dp,
                end = 4.dp,
                bottom = 8.dp
            )
            .width(140.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = { onProductClick(product.id, product.category) })
                .padding(8.dp)
        ) {
            ProductImage(
                imageUrl = product.iconUrl,
                elevation = 4.dp,
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
            Text(
                text = product.name,
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center,
                color = StoreAppTheme.colors.textSecondary,
                modifier = Modifier
                    .width(130.dp)
                    .padding(top = 8.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }
    }
}

@Composable
fun ProductItemShimmer(
    modifier: Modifier = Modifier
) {
    StoreAppSurface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .padding(
                start = 4.dp,
                end = 4.dp,
                bottom = 8.dp
            )
            .width(140.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(ShimmerEffect()))
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier
                .width(130.dp)
                .height(20.dp)
                .padding(horizontal = 8.dp)
                .clip(CircleShape)
                .background(ShimmerEffect()))
        }
    }
}

@Composable
private fun HighlightedProductItem(
    product: Product,
    onProductClick: (Long, String) -> Unit,
    index: Int,
    gradient: List<Color>,
    gradientWidth: Float,
    scroll: Int,
    modifier: Modifier = Modifier
) {
    val left = index * with(LocalDensity.current) {
        (HighlightCardWidth + HighlightCardPadding).toPx()
    }
    StoreAppCard(
        modifier = modifier
            .size(
                width = 170.dp,
                height = 230.dp
            )
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = { onProductClick(product.id, product.category) })
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxSize()
            ) {
                val gradientOffset = left - (scroll / 3f)
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .offsetGradientBackground(gradient, gradientWidth, gradientOffset)
                )
                ProductImage(
                    imageUrl = product.iconUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.BottomCenter)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Center,
                color = StoreAppTheme.colors.textSecondary,
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
            )
        }
    }
}

@Composable
private fun HighlightedProductItemShimmer(
    modifier: Modifier = Modifier
) {
    StoreAppCard(
        modifier = modifier
            .size(
                width = 170.dp,
                height = 250.dp
            )
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = modifier
                    .padding(horizontal = 16.dp)
                    .size(120.dp)
                    .clip(CircleShape)
                    .align(Alignment.BottomCenter)
                    .background(ShimmerEffect()))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Spacer(modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(20.dp)
                .clip(CircleShape)
                .background(ShimmerEffect()))
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(20.dp)
                .clip(CircleShape)
                .background(ShimmerEffect()))
        }
    }
}

@Composable
fun ProductImage(
    imageUrl: String,
    shape: Shape = MaterialTheme.shapes.medium,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    contentScale: ContentScale = ContentScale.Crop
) {
    StoreAppSurface(
        color = Color.LightGray,
        elevation = elevation,
        shape = shape,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
//            placeholder = painterResource(id = R.color.black),
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale
        )
    }
}

@Composable
private fun ViewMoreHighlightCard(
    modifier: Modifier,
    onProductList: (String) -> Unit,
    productCollectionCategory: String
) {
    StoreAppCard(
        modifier = modifier
            .size(
                width = 170.dp,
                height = 230.dp
            )
            .padding(bottom = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onProductList(MainDestinations.PRODUCT_LIST_ROUTE + "/" + productCollectionCategory) },
            contentAlignment = Alignment.Center,
        ) {
            IconButton(onClick = { onProductList(MainDestinations.PRODUCT_LIST_ROUTE + "/" + productCollectionCategory) }) {
                Icon(
                    imageVector = mirroringIcon(
                        ltrIcon = Icons.Outlined.ReadMore,
                        rtlIcon = Icons.Outlined.ReadMore
                    ),
                    tint = StoreAppTheme.colors.brand,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun ViewMoreCard(
    modifier: Modifier,
    onProductList: (String) -> Unit,
    productCollectionCategory: String
) {
    StoreAppSurface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .padding(
                start = 4.dp,
                end = 4.dp,
                bottom = 8.dp
            )
            .width(100.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(onClick = { onProductList(MainDestinations.PRODUCT_LIST_ROUTE + "/" + productCollectionCategory) })
                .padding(8.dp)
        ) {
            StoreAppSurface(
                color = StoreAppTheme.colors.uiBackground.copy(AlphaNearOpaque),
                shape = CircleShape,
                modifier = modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { onProductList(MainDestinations.PRODUCT_LIST_ROUTE + "/" + productCollectionCategory) }) {
                    Icon(
                        imageVector = mirroringIcon(
                            ltrIcon = Icons.Outlined.ReadMore,
                            rtlIcon = Icons.Outlined.ReadMore
                        ),
                        tint = StoreAppTheme.colors.brand,
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview("default")
@Composable
private fun ProductCardPreview() {
    StoreAppTheme {
        val product = products.first()
        HighlightedProductItem(
            product = product,
            onProductClick = { id, category -> },
            index = 0,
            gradient = StoreAppTheme.colors.gradientDarkLavander,
            gradientWidth = gradientWidth,
            scroll = 0
        )
//        ViewMoreCard(
//            modifier = Modifier,
//            onProductList = {},
//            productCollectionCategory = "Untitled"
//        )
    }
}

@Preview("default")
@Composable
private fun ProductCardSimplePreview() {
    StoreAppTheme {
        val product = products.first()
        ProductItem(
            product = product,
            onProductClick = { id, category -> },
        )
    }
}
@Preview("default")
@Composable
private fun ProductCardShimmerPreview() {
    StoreAppTheme {
        HighlightedProductItemShimmer()
    }
}
@Preview("default")
@Composable
private fun ProductCardSimpleShimmerPreview() {
    StoreAppTheme {
        ProductItemShimmer()
    }
}