package uwu.victoraso.storeapp.ui.home.search

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.SearchCategory
import uwu.victoraso.storeapp.model.SearchCategoryCollection
import uwu.victoraso.storeapp.ui.components.ProductImage
import uwu.victoraso.storeapp.ui.components.StoreAppSurface
import uwu.victoraso.storeapp.ui.components.VerticalGrid
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import java.lang.Math.max

@Composable
fun SearchCategories(
    categories: List<SearchCategoryCollection>
) {
    LazyColumn {
        itemsIndexed(categories) { index, collection ->
            SearchCategoryCollection(collection, index)
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun SearchCategoryCollection(
    collection: SearchCategoryCollection,
    index: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = collection.name,
            style = MaterialTheme.typography.h6,
            color = StoreAppTheme.colors.textPrimary,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(horizontal = 24.dp, vertical = 4.dp)
                .wrapContentHeight()
        )
        VerticalGrid(Modifier.padding(horizontal = 16.dp)) {
            val gradient = when (index % 2) {
                0 -> StoreAppTheme.colors.gradient2_2
                else -> StoreAppTheme.colors.gradient2_3
            }
            collection.categories.forEach { category ->
                SearchCategory(
                    category = category,
                    gradient = gradient,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

private val CategoryShape = RoundedCornerShape(10.dp)

@Composable
private fun SearchCategory(
    category: SearchCategory,
    gradient: List<Color>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1.45f)
            .shadow(elevation = 3.dp, shape = CategoryShape)
            .clip(CategoryShape)
            .background(Brush.horizontalGradient(gradient))
            .clickable { /* todo */ },
        contentAlignment = Alignment.Center
    ) {
        CategoryImage(imageUrl = category.imageUrl, contentDescription = "CategoryImage")
        Text(
            text = category.name,
            color = StoreAppTheme.colors.textSecondary,
            style = MaterialTheme.typography.h5,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 4.dp),
            fontSize = 16.sp,
            softWrap = true,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CategoryImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    StoreAppSurface(
        color = Color.LightGray,
        elevation = elevation,
        shape = RectangleShape,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 5.dp),
            contentScale = ContentScale.Crop
        )//TODO: cambiar placeholder
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun SearchCategoryPreview() {
    StoreAppTheme {
        SearchCategory(
            category = SearchCategory(
                name = "Desserts",
                imageUrl = ""
            ),
            gradient = StoreAppTheme.colors.gradient3_2
        )
    }
}