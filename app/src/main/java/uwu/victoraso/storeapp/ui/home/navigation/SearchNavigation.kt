package uwu.victoraso.storeapp.ui.home.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import uwu.victoraso.storeapp.ui.home.search.Search

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(TopLevelDestination.SEARCH.route, navOptions)
}

fun NavGraphBuilder.searchScreen(
    onProductSelected: (Long, String, NavBackStackEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    composable(TopLevelDestination.SEARCH.route) { from ->
        Search(
            onProductClick = { id, category -> onProductSelected(id, category, from) },
            modifier = modifier,
        )
    }
}