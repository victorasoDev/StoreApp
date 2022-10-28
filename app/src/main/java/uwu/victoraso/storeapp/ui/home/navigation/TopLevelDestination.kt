package uwu.victoraso.storeapp.ui.home.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import uwu.victoraso.storeapp.R

enum class TopLevelDestination(
    @StringRes val title: Int,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val route: String
) {
    FEED(R.string.home_feed, Icons.Outlined.Home, Icons.Filled.Home, "home/feed"),
    SEARCH(R.string.home_search, Icons.Outlined.Search, Icons.Filled.Search, "home/search"),
    CART(R.string.home_cart, Icons.Outlined.ShoppingCart, Icons.Filled.ShoppingCart, "home/cart"),
    PROFILE(R.string.home_profile, Icons.Outlined.AccountCircle, Icons.Filled.AccountCircle, "home/profile")
}