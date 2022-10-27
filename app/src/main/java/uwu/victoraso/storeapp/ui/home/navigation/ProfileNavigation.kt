package uwu.victoraso.storeapp.ui.home.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import uwu.victoraso.storeapp.ui.home.profile.Profile

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(TopLevelDestination.PROFILE.route, navOptions)
}

fun NavGraphBuilder.profileScreen(
    restartApp: (String) -> Unit,
    onNavigateTo: (String, NavBackStackEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    composable(TopLevelDestination.PROFILE.route) { from ->
        Profile(
            restartApp = { route -> restartApp(route) },
            onNavigateTo = { route -> onNavigateTo(route, from) },
            modifier = modifier,
        )
    }
}