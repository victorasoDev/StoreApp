package uwu.victoraso.storeapp.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.CollectionType
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.ProductCollection
import uwu.victoraso.storeapp.model.products
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
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    index: Int = 0,
    highlight: Boolean = true
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
            IconButton(
                onClick = { /*TODO*/ },
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
        if (highlight && productCollection.type == CollectionType.Highlight) {
            HighlightedProducts(index = index, products = productCollection.products, onProductClick = onProductClick)
        } else {
            Products(products = productCollection.products, onProductClick = onProductClick)
        }
    }
}

private val HighlightCardWidth = 170.dp
private val HighlightCardPadding = 16.dp

@Composable
private fun HighlightedProducts(
    index: Int,
    products: List<Product>,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val scroll = rememberScrollState(0)
    val gradient = when ((index / 2) % 2) {
        0 -> StoreAppTheme.colors.gradient6_1
        else -> StoreAppTheme.colors.gradient6_2
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
            HighlightedProductItem(
                product = product,
                onProductClick = onProductClick,
                index = index,
                gradient = gradient,
                gradientWidth = gradientWidth,
                scroll = scroll.value
            )
        }
    }
}

@Composable
private fun Products(
    products: List<Product>,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
    ) {
        items(products) { product ->
            ProductItem(product = product, onProductClick = onProductClick)
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    StoreAppSurface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.padding(
            start = 4.dp,
            end = 4.dp,
            bottom = 8.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(onClick = { onProductClick(product.id) })
                .padding(8.dp)
        ) {
            //TODO:
            Text(
                text = product.name,
                style = MaterialTheme.typography.subtitle1,
                color = StoreAppTheme.colors.textSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun HighlightedProductItem(
    product: Product,
    onProductClick: (Long) -> Unit,
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
                height = 250.dp
            )
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = { onProductClick(product.id) })
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
                    imageUrl = product.imageUrl,
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
                style = MaterialTheme.typography.h6,
                color = StoreAppTheme.colors.textSecondary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.tagline,
                style = MaterialTheme.typography.body1,
                color = StoreAppTheme.colors.textHelp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun ProductImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    StoreAppSurface(
        color = Color.LightGray,
        elevation = elevation,
        shape = CircleShape,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )//TODO: cambiar placeholder
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun ProductCardPreview() {
    StoreAppTheme {
        val product = products.first()
        HighlightedProductItem(
            product = product,
            onProductClick = { },
            index = 0,
            gradient = StoreAppTheme.colors.gradient6_1,
            gradientWidth = gradientWidth,
            scroll = 0
        )
    }
}