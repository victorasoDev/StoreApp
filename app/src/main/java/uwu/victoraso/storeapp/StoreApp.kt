package uwu.victoraso.storeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uwu.victoraso.storeapp.ui.components.StoreAppScaffold
import uwu.victoraso.storeapp.ui.components.StoreAppSnackbar
import uwu.victoraso.storeapp.ui.home.StoreAppBottomBar
import uwu.victoraso.storeapp.ui.home.addHomeGraph
import uwu.victoraso.storeapp.ui.home.navigation.TopLevelDestination
import uwu.victoraso.storeapp.ui.home.profile.purchasehistory.PurchaseHistory
import uwu.victoraso.storeapp.ui.home.profile.wishlist.Wishlist
import uwu.victoraso.storeapp.ui.log.login.LoginScreen
import uwu.victoraso.storeapp.ui.log.signup.SignUpScreen
import uwu.victoraso.storeapp.ui.productcollection.ProductList
import uwu.victoraso.storeapp.ui.productcollection.ProductListViewModel
import uwu.victoraso.storeapp.ui.productcreate.ProductCreate
import uwu.victoraso.storeapp.ui.productcreate.ProductCreateViewModel
import uwu.victoraso.storeapp.ui.productdetail.ProductDetail
import uwu.victoraso.storeapp.ui.splash.Splash
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

@Composable
fun StoreApp(
    darkTheme: Boolean,
    appState: StoreAppState = rememberStoreAppState()
) {
    StoreAppTheme(darkTheme = darkTheme) {
        StoreAppScaffold(
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    Column(Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(2.dp).fillMaxWidth().background(color = StoreAppTheme.colors.brand.copy(alpha = 0.2f)))
                        StoreAppBottomBar(
                            destinations = appState.topLevelDestinations,
                            currentDestination = appState.currentDestination,
                            onNavigateToDestination = appState::navigateToBottomBarRoute
                        )
                    }

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
                startDestination = MainDestinations.SPLASH_ROUTE,
                modifier = Modifier.padding(innerPaddingModifier)
            ) {
                storeAppNavGraph(
                    onProductSelected = appState::navigateToProductDetail,
                    onProductCreate = appState::navigateToCreateProduct,
                    onPopUp = appState::navigateAndPopUp,
                    onClearAndNavigate = appState::clearAndNavigate,
                    onNavigateTo = appState::navigateTo,
                    upPress = appState::upPress
                )
            }
        }
    }
}

private fun NavGraphBuilder.storeAppNavGraph(
    onProductSelected: (Long, String, NavBackStackEntry) -> Unit,
    onProductCreate: (NavBackStackEntry) -> Unit, //TODO: cambiar por el navigateTo
    onPopUp: (String, String) -> Unit,
    onClearAndNavigate: (String) -> Unit,
    onNavigateTo: (String, NavBackStackEntry) -> Unit, //TODO: para abrir otras ventanas puede servir
    upPress: () -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = TopLevelDestination.FEED.route
    ) {
        addHomeGraph(onProductSelected, onProductCreate, onNavigateTo, onClearAndNavigate, onNavigateTo)
    }
    composable(
        route = "${MainDestinations.PRODUCT_DETAIL_ROUTE}/{${MainDestinations.CATEGORY_ID_KEY}}/{${MainDestinations.PRODUCT_ID_KEY}}",
        arguments = listOf(
            navArgument(MainDestinations.PRODUCT_ID_KEY) { type = NavType.LongType },
            navArgument(MainDestinations.CATEGORY_ID_KEY) { type = NavType.StringType }
        )
    ) { navBackStackEntry ->
        ProductDetail(
            upPress = upPress,
            onProductClick = { id, category -> onProductSelected(id, category, navBackStackEntry) },
            onNavigateTo = { route -> onNavigateTo(route, navBackStackEntry) }
        )
    }
    composable(
        route = MainDestinations.PRODUCT_CREATE_ROUTE,
    ) {
        val viewModel: ProductCreateViewModel = hiltViewModel()
        ProductCreate(
            upPress = upPress,
            addNewProduct = viewModel::addNewProduct
        )
    }
    composable(
        route = "${MainDestinations.PRODUCT_LIST_ROUTE}/{${MainDestinations.CATEGORY_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.CATEGORY_ID_KEY) { type = NavType.StringType })
    ) { navBackStackEntry ->
        val argument = requireNotNull(navBackStackEntry.arguments)
        val category = argument.getString(MainDestinations.CATEGORY_ID_KEY)
        ProductListViewModel.categorySelected = category!! //TODO: en product details se le pasa como argumento, cambiar esto

        ProductList(
            onProductSelected = { id -> onProductSelected(id, category, navBackStackEntry) },
            category = category,
            upPress = upPress
        )
    }
    composable(route = MainDestinations.SIGNUP_ROUTE) { navBackStackEntry ->
        SignUpScreen(
            openAndPopUp = { route, popUp -> onPopUp(route, popUp) },
            onNavigateTo = { route -> onNavigateTo(route, navBackStackEntry) }
        )
    }
    composable(route = MainDestinations.LOGIN_ROUTE) {
        LoginScreen(
            onClearAndNavigate = { route -> onClearAndNavigate(route) },
            onNavigateTo = { route -> onClearAndNavigate(route) }
        )
    }
    composable(route = MainDestinations.SPLASH_ROUTE) {
        Splash(openAndPopUp = { route, from -> onPopUp(route, from) })
    }

    /** Profile destinations **/
    composable(route = MainDestinations.WISHLIST_ROUTE) { navBackStackEntry ->
        /** En este se deuelve también la categoría porque no hay otra forma de obtener la categoría de un producto de la wishlist **/
        Wishlist(
            upPress = upPress,
            onProductSelected = { id, category -> onProductSelected(id, category, navBackStackEntry) }
        )
    }
    composable(route = MainDestinations.PURCHASE_HISTORY_ROUTE) { navBackStackEntry ->
        PurchaseHistory(
            upPress = upPress,
            onProductSelected = { id, category -> onProductSelected(id, category, navBackStackEntry) }
        )
    }
}