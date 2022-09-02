package uwu.victoraso.storeapp.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import uwu.victoraso.storeapp.R

class Filter(
    val name: String,
    enabled: Boolean = false,
    val icon: ImageVector? = null
) {
    val enabled = mutableStateOf(enabled)
}

val filters = listOf(
    Filter(name = "Organic"),
    Filter(name = "Gluten-free"),
    Filter(name = "Dairy-free"),
    Filter(name = "Sweet"),
    Filter(name = "Savory")
)

val priceFilters = listOf(
    Filter(name = "$"),
    Filter(name = "$$"),
    Filter(name = "$$$"),
    Filter(name = "$$$$")
)

val sortFilters = listOf(
    Filter(name = "Android's favorite (default)", icon = Icons.Filled.Android),
    Filter(name = "Rating", icon = Icons.Filled.Star),
    Filter(name = "Alphabetical", icon = Icons.Filled.SortByAlpha)
)
val categoryFilters = listOf(
    Filter(name = "Chips & crackers"),
    Filter(name = "Fruit snacks"),
    Filter(name = "Desserts"),
    Filter(name = "Nuts")
)
val lifeStyleFilters = listOf(
    Filter(name = "Organic"),
    Filter(name = "Gluten-free"),
    Filter(name = "Dairy-free"),
    Filter(name = "Sweet"),
    Filter(name = "Savory")
)

@Composable
fun StoreAppFilters() = listOf(
    Filter(stringResource(id = R.string.product_type_motherboard)),
    Filter(stringResource(id = R.string.product_type_processor)),
    Filter(stringResource(id = R.string.product_type_graphics)),
    Filter(stringResource(id = R.string.product_type_storage)),
    Filter(stringResource(id = R.string.product_type_cooling)),
    Filter(stringResource(id = R.string.product_type_ram)),
    Filter(stringResource(id = R.string.product_type_laptop)),
    Filter(stringResource(id = R.string.product_type_computer)),
    Filter(stringResource(id = R.string.product_type_monitors)),
    Filter(stringResource(id = R.string.product_type_mouse)),
    Filter(stringResource(id = R.string.product_type_keyboard))
)

var sortDefault = sortFilters[0].name
