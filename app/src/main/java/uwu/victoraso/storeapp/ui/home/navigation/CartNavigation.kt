package uwu.victoraso.storeapp.ui.home.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import uwu.victoraso.storeapp.ui.home.cart.Cart

fun NavController.navigateToCart(navOptions: NavOptions? = null) {
    this.navigate(TopLevelDestination.CART.route, navOptions)
}

fun NavGraphBuilder.cartScreen(
    onProductSelected: (Long, String, NavBackStackEntry) -> Unit,
    onProductList: (String, NavBackStackEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    composable(TopLevelDestination.CART.route) { from ->
        Cart(
            onProductClick = { id, category -> onProductSelected(id, category, from) },
            modifier = modifier,
            onProductList = { category -> onProductList(category, from) },
        )
    }
}