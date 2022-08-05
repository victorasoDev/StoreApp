package uwu.victoraso.storeapp.ui.home.search

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import uwu.victoraso.storeapp.model.SearchCategory
import uwu.victoraso.storeapp.model.SearchCategoryCollection
import uwu.victoraso.storeapp.ui.components.ProductImage
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

            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

private val MinImageSize = 134.dp
private val CategoryShape = RoundedCornerShape(10.dp)
private const val CategoryTextProportion = 0.55f

@Composable
private fun SearchCategory(
    category: SearchCategory,
    gradient: List<Color>,
    modifier: Modifier = Modifier
) {
    Layout(
        modifier = modifier
            .aspectRatio(1.45f)
            .shadow(elevation = 3.dp, shape = CategoryShape)
            .clip(CategoryShape)
            .background(Brush.horizontalGradient(gradient))
            .clickable { /* todo */ },
        content = {
            Text(
                text = category.name,
                style = MaterialTheme.typography.subtitle1,
                color = StoreAppTheme.colors.textSecondary,
                modifier = Modifier
                    .padding(4.dp)
                    .padding(start = 8.dp)
            )
            ProductImage(
                imageUrl = category.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    ) { measurables, constraints ->
        // Text given a set proportion of width (which is determined by the aspect ratio)
        val textWidth = (constraints.maxWidth * CategoryTextProportion).toInt()
        val textPlaceable = measurables[0].measure(Constraints.fixedWidth(textWidth))

        // Image is sized to the larger of height of item, or a minimum value
        // i.e. may appear larger than item (but clipped to the item bounds)
        val imageSize = kotlin.math.max(MinImageSize.roundToPx(), constraints.maxHeight)
        val imagePlaceable = measurables[1].measure(Constraints.fixed(imageSize, imageSize))
        layout(
            width = constraints.maxWidth,
            height = constraints.minHeight
        ) {
            textPlaceable.placeRelative(
                x = 0,
                y = (constraints.maxHeight - textPlaceable.height) / 2 //centered
            )
            imagePlaceable.placeRelative(
                // image is placed to end of text i.e. will overflow to the end (but be clipped)
                x = textWidth,
                y = (constraints.maxHeight - imagePlaceable.height) / 2 //centered
            )
        }
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