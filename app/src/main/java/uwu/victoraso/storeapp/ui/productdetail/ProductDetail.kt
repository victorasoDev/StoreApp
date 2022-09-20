package uwu.victoraso.storeapp.ui.productdetail

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.firestore.FirebaseFirestore
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.ProductCollection
import uwu.victoraso.storeapp.model.ProductRepo
import uwu.victoraso.storeapp.repositories.products.ProductDataSource
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import uwu.victoraso.storeapp.ui.components.*
import uwu.victoraso.storeapp.ui.theme.Neutral8
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.formatPrice
import uwu.victoraso.storeapp.ui.utils.mirroringBackIcon

private val BottomBarHeight = 56.dp
private val TitleHeight = 128.dp
private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 300.dp
private val CollapsedImageSize = 150.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ProductDetail(
    upPress: () -> Unit,
    viewModel: ProductDetailViewModel
) {
    val productDetailScreenUiState: ProductDetailScreenUiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (productDetailScreenUiState.product) {
        is ProductDetailUiState.Success -> {
            val product = (productDetailScreenUiState.product as ProductDetailUiState.Success).product
            val relatedProducts = if (productDetailScreenUiState.relatedProducts is RelatedProductsUiState.Success) {
                    (productDetailScreenUiState.relatedProducts as RelatedProductsUiState.Success).productCollection
                } else {
                    ProductCollection(0L, "No related")
                }

            Box(modifier = Modifier.fillMaxSize()) {
                val scroll = rememberScrollState(0)
                Header()
                Body(listOf(relatedProducts), scroll) // TODO: el body espera un par de colecciones
                Title(product) { scroll.value }
                Image(product.imageUrl) { scroll.value }
                Up(upPress)
                CartBottomBar(modifier = Modifier.align(Alignment.BottomCenter))
            }
        }
        is ProductDetailUiState.Loading -> {

        }
        is ProductDetailUiState.Error -> {

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
    related: List<ProductCollection>,
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
                        text = stringResource(R.string.detail_header),
                        style = MaterialTheme.typography.overline,
                        color = StoreAppTheme.colors.textHelp,
                        modifier = HzPadding.padding(top = 20.dp) //TODO: cambiar? cuando el nombre tiene dos lineas, este texto desaparece sin el padding 20
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

                    related.forEach { productCollection ->
                        key(productCollection.id) {
                            ProductCollection(
                                productCollection = productCollection,
                                onProductList = { },
                                onProductClick = { },
                                highlight = false
                            )
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
    scrollProvider: () -> Int
) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .statusBarsPadding()
            .offset {
                val scroll = scrollProvider()
                val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                IntOffset(x = 0, y = offset.toInt())
            }
            .background(color = StoreAppTheme.colors.uiBackground)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = product.name,
            style = MaterialTheme.typography.h4,
            color = StoreAppTheme.colors.textSecondary,
            modifier = HzPadding
        )
        Text(
            text = product.tagline,
            style = MaterialTheme.typography.subtitle2,
            fontSize = 20.sp,
            color = StoreAppTheme.colors.textHelp,
            modifier = HzPadding
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = formatPrice(product.price),
            style = MaterialTheme.typography.h6,
            color = StoreAppTheme.colors.textPrimary,
            modifier = HzPadding
        )
        Spacer(modifier = Modifier.height(8.dp))
        StoreAppDivider()
    }
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
private fun CartBottomBar(modifier: Modifier = Modifier) {
    val (count, updateCount) = remember { mutableStateOf(1) }
    StoreAppSurface(modifier = modifier) {
        Column {
            StoreAppDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .navigationBarsPadding()
                    .then(HzPadding)
                    .heightIn(min = BottomBarHeight)
            ) {
                QuantitySelector(
                    count = count,
                    decreaseItemCount = { if (count > 0) updateCount(count - 1) },
                    increaseItemCount = { updateCount(count + 1) }
                )
                Spacer(modifier = Modifier.width(16.dp))
                StoreAppButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.add_to_cart),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }

        }
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun SnackDetailPreview() {
    StoreAppTheme {
        ProductDetail(
            upPress = { },
            viewModel = ProductDetailViewModel(
                ProductRepository(FirebaseFirestore.getInstance(), ProductDataSource(FirebaseFirestore.getInstance())),
                SavedStateHandle()
                )
        )
    }
}