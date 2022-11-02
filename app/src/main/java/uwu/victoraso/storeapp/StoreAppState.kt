package uwu.victoraso.storeapp

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import uwu.victoraso.storeapp.model.SnackbarManager
import uwu.victoraso.storeapp.ui.home.navigation.*
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG_CART

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val PRODUCT_DETAIL_ROUTE = "product"
    const val PRODUCT_ID_KEY = "productId"
    const val PRODUCT_CREATE_ROUTE = "productCreate"
    const val PRODUCT_LIST_ROUTE = "productList"
    const val CATEGORY_ID_KEY = "categoryId"
    const val LOGIN_ROUTE = "login"
    const val SIGNUP_ROUTE = "signup"
    const val SPLASH_ROUTE = "splash"
    const val PERSONAL_INFO_ROUTE = "personalInfo"
    const val WISHLIST_ROUTE = "wishlist"
    const val PURCHASE_HISTORY_ROUTE = "purchaseHistory"
    const val RECOMMENDED_ROUTE = "recommended"
    const val SETTINGS_ROUTE = "settings"
}

@Composable
fun rememberStoreAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(scaffoldState, navController, snackbarManager, resources, coroutineScope) {
        StoreAppState(scaffoldState, navController, snackbarManager, resources, coroutineScope)
    }

@Stable
class StoreAppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    // Process snackbars coming from SnackbarManager
    init {
        coroutineScope.launch {
            snackbarManager.messages.collect { currentMessages ->
                if (currentMessages.isNotEmpty()) {
                    val message = currentMessages[0]
                    val text = resources.getText(message.messageId)

                    // Display the snackbar on the screen. `showSnackbar` is a function
                    // that suspends until the snackbar disappears from the screen
                    scaffoldState.snackbarHostState.showSnackbar(text.toString())
                    // Once the snackbar is gone or dismissed, notify the SnackbarManager
                    snackbarManager.setMessageShown(message.id)
                }
            }
        }
    }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    val bottomBarTabs = TopLevelDestination.values()
    private val bottomBarRoutes = bottomBarTabs.map { it.route } // just routes arraylist

    // Reading this attribute will cause recompositions when the bottom bar needs shown, or not.
    // Not all routes need to show the bottom bar.
    val shouldShowBottomBar: Boolean
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToBottomBarRoute(topLevelDestination: TopLevelDestination) {
        val topLevelOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                Log.d(DEBUG_TAG_CART, "ID -> ${navController.graph.findStartDestination().id}")
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
        when (topLevelDestination) {
            TopLevelDestination.FEED -> navController.navigateToFeed(navOptions = topLevelOptions)
            TopLevelDestination.SEARCH -> navController.navigateToSearch(navOptions = topLevelOptions)
            TopLevelDestination.CART -> navController.navigateToCart(navOptions = topLevelOptions)
            TopLevelDestination.PROFILE -> navController.navigateToProfile(navOptions = topLevelOptions)
        }
    }

    fun navigateToProductDetail(productId: Long, category: String, from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, check the lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.PRODUCT_DETAIL_ROUTE}/$category/$productId")
        }
    }

    fun navigateToCreateProduct(from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, check the lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainDestinations.PRODUCT_CREATE_ROUTE)
        }
    }

    fun navigateTo(route: String, from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, check the lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate(route) {
                launchSingleTop = route == MainDestinations.LOGIN_ROUTE
            }
        }
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

/**
 * Copied from similar function in NavigationUI.kt
 *
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:navigation/navigation-ui/src/main/java/androidx/navigation/ui/NavigationUI.kt
 */
private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}

/**
 * A composable function that returns the [Resources]. It will be recomposed when `Configuration`
 * gets updated.
 */
@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@Composable
fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> isSystemInDarkTheme()
    is MainActivityUiState.Success -> when (uiState.darkTheme) {
        true -> true
        false -> false
    }
}