package uwu.victoraso.storeapp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import uwu.victoraso.storeapp.ui.productcollection.ProductList
import uwu.victoraso.storeapp.ui.productdetail.ProductDetail
import uwu.victoraso.storeapp.ui.components.StoreAppScaffold
import uwu.victoraso.storeapp.ui.components.StoreAppSnackbar
import uwu.victoraso.storeapp.ui.home.HomeSections
import uwu.victoraso.storeapp.ui.home.StoreAppBottomBar
import uwu.victoraso.storeapp.ui.home.addHomeGraph
import uwu.victoraso.storeapp.ui.productcollection.ProductListViewModel
import uwu.victoraso.storeapp.ui.productcreate.ProductCreate
import uwu.victoraso.storeapp.ui.productcreate.ProductCreateViewModel
import uwu.victoraso.storeapp.ui.productdetail.ProductDetailViewModel
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
                    onProductCreate = appState::navigateToCreateProduct,
                    onProductList = appState::navigateToProductList,
                    upPress = appState::upPress
                )
            }
        }
    }
}

private fun NavGraphBuilder.storeAppNavGraph(
    onProductSelected: (Long, NavBackStackEntry) -> Unit,
    onProductCreate: (NavBackStackEntry) -> Unit,
    onProductList: (String, NavBackStackEntry) -> Unit,
    upPress: () -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.FEED.route
    ) {
        addHomeGraph(onProductSelected, onProductCreate, onProductList)
    }
    composable(
        route = "${MainDestinations.PRODUCT_DETAIL_ROUTE}/{${MainDestinations.PRODUCT_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.PRODUCT_ID_KEY) { type = NavType.LongType })
    ) { navBackStackEntry ->
        val viewModel: ProductDetailViewModel = hiltViewModel()

        ProductDetail(upPress, viewModel)
    }
    composable(
        route = MainDestinations.PRODUCT_CREATE_ROUTE,
    ) {
        val viewModel: ProductCreateViewModel = hiltViewModel()
        val state = viewModel.state
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
        ProductListViewModel.categorySelected = category!!

        val viewModel: ProductListViewModel = hiltViewModel()
        ProductList(
            onProductSelected = { id -> onProductSelected(id, navBackStackEntry) },
            category = category,
            upPress = upPress, //TODO esto no va
            viewModel = viewModel
        )
    }
}