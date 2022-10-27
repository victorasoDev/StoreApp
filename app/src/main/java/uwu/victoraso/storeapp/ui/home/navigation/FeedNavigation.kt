package uwu.victoraso.storeapp.ui.home.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import uwu.victoraso.storeapp.ui.home.feed.Feed

fun NavController.navigateToFeed(navOptions: NavOptions? = null) {
    this.navigate(TopLevelDestination.FEED.route, navOptions)
}

fun NavGraphBuilder.feedScreen(
    onProductSelected: (Long, String, NavBackStackEntry) -> Unit,
    onProductCreate: (NavBackStackEntry) -> Unit,
    onNavigateTo: (String, NavBackStackEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    composable(TopLevelDestination.FEED.route) { from ->
        Feed(
            onProductClick = { id, category -> onProductSelected(id, category, from) },
            onProductCreate = { onProductCreate(from) },
            onNavigateTo = { category -> onNavigateTo(category, from) },
            modifier = modifier,
        )
    }
}