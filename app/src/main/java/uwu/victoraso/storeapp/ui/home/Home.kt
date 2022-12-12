package uwu.victoraso.storeapp.ui.home

import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uwu.victoraso.storeapp.ui.home.navigation.*
import uwu.victoraso.storeapp.ui.theme.NoRippleTheme
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

fun NavGraphBuilder.addHomeGraph(
    onProductSelected: (Long, String, NavBackStackEntry) -> Unit,
    onProductList: (String, NavBackStackEntry) -> Unit,
    restartApp: (String) -> Unit,
    onNavigateTo: (String, NavBackStackEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    feedScreen(
        onProductSelected = onProductSelected,
        onNavigateTo = onNavigateTo,
        modifier = modifier,
    )
    searchScreen(
        onProductSelected = onProductSelected,
        onNavigateTo = onNavigateTo,
        modifier = modifier
    )
    cartScreen(
        onProductSelected = onProductSelected,
        onProductList = onProductList,
        modifier = modifier
    )
    profileScreen(
        restartApp = restartApp,
        onNavigateTo = onNavigateTo,
        modifier = modifier
    )
}

@Composable
fun StoreAppBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?
) {
    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        StoreAppNavigationBar {
            destinations.forEach { destination ->
                val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
                StoreAppNavigationBarItem(
                    selected = selected,
                    onClick = { onNavigateToDestination(destination) },
                    icon = {
                        Icon(
                            imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                            contentDescription = stringResource(id = destination.title)
                        )
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun StoreAppBottomNavPreview() {
    StoreAppTheme {
        StoreAppBottomBar(
            destinations = listOf(TopLevelDestination.FEED, TopLevelDestination.SEARCH, TopLevelDestination.CART, TopLevelDestination.PROFILE),
            currentDestination = rememberNavController().currentBackStackEntryAsState().value?.destination,
            onNavigateToDestination = { }
        )
    }
}