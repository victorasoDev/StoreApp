package uwu.victoraso.storeapp.ui.home.profile.purchasehistory

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
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
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG

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

@Composable
fun PurchaseListItem(
    purchase: Purchase,
    onProductSelected: (Long, String) -> Unit
) {
    Log.d(DEBUG_TAG, "recompose PurchaseListItem")
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .clickable { isExpanded = !isExpanded }
            .padding(vertical = 8.dp)
            .padding(start = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = purchase.date,
                style = MaterialTheme.typography.subtitle1,
                color = StoreAppTheme.colors.textSecondary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth()
            )
            Text(
                text = purchase.userAdress,
                style = MaterialTheme.typography.subtitle1,
                color = StoreAppTheme.colors.textPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(start = 8.dp)
            )
            Log.d(DEBUG_TAG, "isExpanded $isExpanded")
            if (isExpanded) {
                Column(Modifier.background(StoreAppTheme.colors.brand.copy(alpha = 0.1f))) {
                    purchase.products.map { product ->
                        ProductListItem(
                            product = product,
                            onProductSelected = onProductSelected,
                            small = true
                        )
                    }
                }
            }
        }
    }
    StoreAppDivider(modifier = Modifier.fillMaxWidth())
}