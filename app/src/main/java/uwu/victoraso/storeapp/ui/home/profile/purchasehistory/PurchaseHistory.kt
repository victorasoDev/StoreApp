package uwu.victoraso.storeapp.ui.home.profile.purchasehistory

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.Purchase
import uwu.victoraso.storeapp.ui.components.StoreAppCircularIndicator
import uwu.victoraso.storeapp.ui.components.StoreAppDivider
import uwu.victoraso.storeapp.ui.components.StoreAppSurface
import uwu.victoraso.storeapp.ui.components.StoreAppTopBar
import uwu.victoraso.storeapp.ui.productcollection.ProductListItem
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.formatDate
import uwu.victoraso.storeapp.ui.utils.formatPrice

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun PurchaseHistory(
    upPress: () -> Unit,
    onProductSelected: (Long, String) -> Unit,
    viewModel: PurchaseHistoryViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val purchaseHistoryUiState: PurchaseHistoryUiState by viewModel.purchaseHistoryUiState.collectAsStateWithLifecycle()

    StoreAppSurface(modifier = modifier.fillMaxSize()) {
        when (purchaseHistoryUiState) {
            is PurchaseHistoryUiState.Success -> {
                Box {
                    PurchaseHistory(
                        purchaseHistory = (purchaseHistoryUiState as PurchaseHistoryUiState.Success).purchases,
                        onProductSelected = onProductSelected,
                        modifier = modifier
                    )
                    StoreAppTopBar(upPress = upPress, screenTitle = stringResource(id = R.string.user_profile_purchase_history))

                }
            }
            is PurchaseHistoryUiState.Loading -> {
                Box(modifier = modifier.fillMaxSize()) { StoreAppCircularIndicator(modifier = modifier.align(Alignment.Center)) }
            }
            is PurchaseHistoryUiState.Error -> {}
        }
    }
}

@Composable
fun PurchaseHistory(
    purchaseHistory: List<Purchase>,
    onProductSelected: (Long, String) -> Unit,
    modifier: Modifier
) {
    Log.d("debugprueba", purchaseHistory.toString())
    purchaseHistory.sortedBy { it.date }
    Log.d("debugprueba", purchaseHistory.toString())
    LazyColumn(modifier = modifier, state = rememberLazyListState()) {
        item {
            Spacer(
                modifier = Modifier.windowInsetsTopHeight(
                    WindowInsets.statusBars.add(WindowInsets(top = 56.dp))
                )
            )
        }
        items(purchaseHistory) { purchase ->
            PurchaseListItem(purchase = purchase, onProductSelected = onProductSelected)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PurchaseListItem(
    purchase: Purchase,
    onProductSelected: (Long, String) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val transition = updateTransition(targetState = isExpanded, label = "expanded")

    Row(
        modifier = Modifier
            .clickable { isExpanded = !isExpanded }
            .padding(vertical = 8.dp)
            .padding(start = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = { isExpanded = !isExpanded }) {
            Row(verticalAlignment = Top) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Open cart items" )
                Text(text = purchase.products.size.toString(), Modifier.align(Top))
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PurchaseDetailTextField(
                text = stringResource(id = R.string.purchase_details),
                textStyle = MaterialTheme.typography.subtitle1,
                paddingStart = 8.dp
            )
            StoreAppDivider(modifier = Modifier.padding(horizontal = 16.dp))
            PurchaseDetailTextField(text = stringResource(id = R.string.purchase_details_name, purchase.userName))
            PurchaseDetailTextField(text = stringResource(id = R.string.purchase_details_adress, purchase.userAdress))
            PurchaseDetailTextField(text = stringResource(id = R.string.purchase_details_date, purchase.date.formatDate()))
            PurchaseDetailTextField(
                text = stringResource(id = R.string.purchase_details_total_price, formatPrice(purchase.price)),
                modifier = Modifier.align(End).padding(end = 16.dp)
            )
            if (isExpanded) {
                transition.AnimatedVisibility(
                    enter = fadeIn(animationSpec = tween(500)) + expandVertically(
                        animationSpec = tween(500)
                    ),
                    exit = fadeOut(animationSpec = tween(500)) + slideOutVertically(
                        animationSpec = tween(500)
                    ),
                    content = {
                        Column(Modifier.background(StoreAppTheme.colors.brand.copy(alpha = 0.1f))) {
                            purchase.products.map { product ->
                                ProductListItem(
                                    product = product,
                                    onProductSelected = onProductSelected,
                                    small = true
                                )
                            }
                        }
                    },
                    visible = { it }
                )
            }
        }
    }
}

@Composable
private fun PurchaseDetailTextField(
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.subtitle2,
    paddingStart: Dp = 16.dp,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = textStyle,
        color = StoreAppTheme.colors.textPrimary,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = modifier.padding(start = paddingStart)
    )
}