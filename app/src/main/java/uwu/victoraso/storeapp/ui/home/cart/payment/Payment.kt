package uwu.victoraso.storeapp.ui.home.cart.payment

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.Cart
import uwu.victoraso.storeapp.model.CartProduct
import uwu.victoraso.storeapp.ui.components.ProductImage
import uwu.victoraso.storeapp.ui.components.StoreAppDialog
import uwu.victoraso.storeapp.ui.components.StoreAppDialogButton
import uwu.victoraso.storeapp.ui.components.StoreAppDialogTitle
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.formatPrice

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun PaymentDialog(
    show: Boolean,
    cart: Cart,
    viewModel: PaymentViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {

    val paymentUiState: PaymentDataUiState by viewModel.paymentUiState.collectAsStateWithLifecycle()

    if (show && paymentUiState is PaymentDataUiState.Success) {
        StoreAppDialog(onDismiss = onDismiss, properties = DialogProperties(dismissOnClickOutside = true)) {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
            ) {
                item {
                    StoreAppDialogTitle(stringID = R.string.cart_payment_title)
                }
                item {
                    LazyRow {
                        items(cart.cartItems) { item ->
                            PaymentProductItem(cartProduct = item)
                        }
                    }
                }
//            items(list) { item ->
//                PersonalInfoItem(item = item)
//            }
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        StoreAppDialogButton(onClick = onDismiss, stringID = R.string.close)
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentProductItem(
    cartProduct: CartProduct,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .padding(all = 16.dp)
            .width(120.dp)
            .background(StoreAppTheme.colors.uiBackground),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProductImage(
            imageUrl = cartProduct.imageUrl,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = cartProduct.name,
                style = MaterialTheme.typography.subtitle1,
                color = StoreAppTheme.colors.textSecondary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .width(120.dp)
            )
            Text(
                text = formatPrice(cartProduct.price),
                style = MaterialTheme.typography.subtitle1,
                color = StoreAppTheme.colors.textPrimary,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
        }
        IconButton(
            onClick = { /*removeProduct(cartProduct.productId, cartProduct.cartId)*/ },
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = StoreAppTheme.colors.iconSecondary
            )
        }
    }
}

@Preview("default", showSystemUi = true)
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CartPreview() {
    StoreAppTheme {
        PaymentProductItem(
            cartProduct = CartProduct(name = "Gygabyte GeForce RTX 3080", price = 1234323)
        )
    }
}