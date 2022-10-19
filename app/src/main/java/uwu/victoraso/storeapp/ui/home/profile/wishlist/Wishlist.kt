package uwu.victoraso.storeapp.ui.home.profile.wishlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.ui.components.StoreAppCircularIndicator
import uwu.victoraso.storeapp.ui.components.StoreAppSurface
import uwu.victoraso.storeapp.ui.components.StoreAppTopBar
import uwu.victoraso.storeapp.ui.productcollection.ProductListItem

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun Wishlist(
    upPress: () -> Unit,
    onProductSelected: (Long, String) -> Unit,
    viewModel: WishlistViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val wishlistUiState: WishlistUiState by viewModel.wishlistUiState.collectAsStateWithLifecycle()

    StoreAppSurface(modifier = modifier.fillMaxSize()) {
        Box {
            when (wishlistUiState) {
                is WishlistUiState.Success -> {
                    val wishlist by remember { mutableStateOf((wishlistUiState as WishlistUiState.Success).wishlist) }
                    Wishlist(
                        onProductSelected = onProductSelected,
                        wishlist = wishlist,
                        modifier = modifier
                    )
                }
                is WishlistUiState.Loading -> {
                    StoreAppCircularIndicator()
                }
                is WishlistUiState.Error -> {
                    //TODO
                }
            }
            StoreAppTopBar(upPress = upPress, screenTitle = "Wishlist")
        }
    }
}

@Composable
private fun Wishlist(
    onProductSelected: (Long, String) -> Unit,
    wishlist: List<Product>,
    modifier: Modifier
) {
    val lazyColumnState = rememberLazyListState()
    LazyColumn(modifier = modifier, state = lazyColumnState) {
        item {
            Spacer(
                modifier = Modifier.windowInsetsTopHeight(
                    WindowInsets.statusBars.add(WindowInsets(top = 56.dp))
                )
            )
            //TODO: el filtro habrá que meterlo por aqui
        }
        items(wishlist) { product ->
            //TODO: posible swipe para añadir (en cart está para eliminar)
            ProductListItem(
                product = product,
                onProductSelected = onProductSelected,
                addProduct = { /** Do nothing **/ },
                removeProduct = { /** TODO **/ }
            )
        }
    }
}