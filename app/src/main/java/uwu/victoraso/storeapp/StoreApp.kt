package uwu.victoraso.storeapp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import uwu.victoraso.storeapp.ui.components.StoreAppScaffold
import uwu.victoraso.storeapp.ui.components.StoreAppSnackbar
import uwu.victoraso.storeapp.ui.home.HomeSections
import uwu.victoraso.storeapp.ui.home.StoreAppBottomBar
import uwu.victoraso.storeapp.ui.home.addHomeGraph
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

@Composable
fun StoreApp() {
    StoreAppTheme {
        val appState = rememberStoreAppState()
        StoreAppScaffold(
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    StoreAppBottomBar(tabs = appState.bottomBarTabs, currentRoute = appState.currentRoute!!, navigateToRoute = appState::navigateToBottomBarRoute)
                }
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = it,
                    modifier = Modifier.systemBarsPadding(),
                    snackbar = { snackbarData -> StoreAppSnackbar(snackbarData = snackbarData) }
                )
            },
            scaffoldState = appState.scaffoldState
        ) { innerPaddingModifier ->
            NavHost(
                navController = appState.navController,
                startDestination = MainDestinations.HOME_ROUTE,
                modifier = Modifier.padding(innerPaddingModifier)
            ) {
                storeAppNavGraph(
                    onProductSelected = appState::navigateToProductDetail,
                    upPress = appState::upPress
                )
            }
        }
    }
}

private fun NavGraphBuilder.storeAppNavGraph(
    onProductSelected: (Long, NavBackStackEntry) -> Unit,
    upPress: () -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.FEED.route
    ) {
        addHomeGraph(onProductSelected)
    }
    composable(
        route = "${MainDestinations.PRODUCT_DETAIL_ROUTE}/${MainDestinations.PRODUCT_ID_KEY}",
        arguments = listOf(navArgument(MainDestinations.PRODUCT_ID_KEY) { type = NavType.LongType })
    ) { navBackStackEntry ->
        val argument = requireNotNull(navBackStackEntry.arguments)
        val productId = argument.getLong(MainDestinations.PRODUCT_ID_KEY)
        //TODO: ProductDetail(productId, upPress)
    }
}