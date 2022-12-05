package uwu.victoraso.storeapp.ui.home.cart.payment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.*
import uwu.victoraso.storeapp.ui.components.*
import uwu.victoraso.storeapp.ui.home.profile.personalinfo.PersonalInfoDialog
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
        val userData = viewModel.getUserDataAsList(userProfile = (paymentUiState as PaymentDataUiState.Success).userProfile)
        val paymentDataItems = GetPaymentDataItems()
        var isPersonalInfoDialogShowing by remember { mutableStateOf(false) }
        val isPaymentLoading by remember { viewModel.isPaymentLoading }
        val isPaymentCompleted by remember { viewModel.isPaymentCompleted }

        PersonalInfoDialog(show = isPersonalInfoDialogShowing, onDismiss = { isPersonalInfoDialogShowing = !isPersonalInfoDialogShowing })

        StoreAppDialog(
            onDismiss = onDismiss,
            properties = DialogProperties(dismissOnClickOutside = true),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Column(Modifier.fillMaxWidth()) {
                StoreAppDialogTitle(stringID = R.string.cart_payment_title)
                PaymentProductsRow(cart, viewModel::removeProduct)
                CustomerData(userData = userData, changePIDialogVisibility = { isPersonalInfoDialogShowing = !isPersonalInfoDialogShowing })
                PaymentData(paymentDataItems = paymentDataItems)
                PaymentDialogButtons(
                    onDismiss = onDismiss,
                    isPaymentLoading = isPaymentLoading,
                    isPaymentCompleted = isPaymentCompleted,
                    makePurchase = viewModel::makePurchase,
                    price = cart.cartItems.sumOf { it.price },
                    cardDetails = CardDetails(
                        cardName = paymentDataItems[0].value,
                        cardNumber = paymentDataItems[1].value,
                        cardExpireDate = paymentDataItems[2].value,
                        cardCVC = paymentDataItems[3].value,
                    ),
                    products = cart.cartItems.map { Product(id = it.productId, name = it.name, price = it.price, iconUrl = it.iconUrl) }
                )
            }
        }
    }
}

@Composable
private fun PaymentProductsRow(
    cart: Cart,
    removeProduct: (Long, Long) -> Unit
) {
    LazyRow {
        items(cart.cartItems) { item ->
            PaymentProductItem(cartProduct = item, cartId = cart.id, removeProduct = removeProduct)
        }
    }
}

@Composable
private fun PaymentDialogButtons(
    onDismiss: () -> Unit,
    isPaymentLoading: Boolean,
    isPaymentCompleted: Boolean,
    makePurchase: (Long, CardDetails, List<Product>) -> Boolean,
    price: Long,
    cardDetails: CardDetails,
    products: List<Product>
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        StoreAppDialogButton(onClick = onDismiss, stringID = R.string.close)
        StoreAppLoadingButton(
            onClick = { makePurchase(price, cardDetails, products) },
            modifier = Modifier.padding(top = 16.dp),
            isLoading = isPaymentLoading,
            enabled = cardDetails.checkCardDetails(),
            defaultText = R.string.payment_action_pay,
            actionText = R.string.payment_action_verifying
        )
    }

    LaunchedEffect(isPaymentLoading) {
        if (isPaymentCompleted) onDismiss()
    }
}

@Composable
private fun PaymentProductItem(
    cartProduct: CartProduct,
    cartId: Long,
    removeProduct: (Long, Long) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .padding(start = 8.dp)
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically,
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
                    .padding(start = 8.dp)
                    .width(100.dp)
            )
            Text(
                text = formatPrice(cartProduct.price),
                style = MaterialTheme.typography.subtitle1,
                color = StoreAppTheme.colors.textPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = null,
            tint = StoreAppTheme.colors.iconSecondary,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .clickable(
                    indication = null,
                    enabled = true,
                    interactionSource = MutableInteractionSource(),
                    onClick = { removeProduct(cartProduct.productId, cartId) }
                )
        )
    }
}

@Composable
private fun CustomerData(
    userData: List<String>,
    changePIDialogVisibility: () -> Unit
) {
    LazyColumn {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.cart_customer_data),
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle1
                )
                Icon(
                    modifier = Modifier
                        .size(14.dp)
                        .clickable(
                            indication = null,
                            enabled = true,
                            interactionSource = MutableInteractionSource(),
                            onClick = { changePIDialogVisibility() }
                        ),
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit ${stringResource(R.string.cart_customer_data)}",
                )

            }
        }
        items(userData) { data ->
            Text(
                text = data,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 2.dp),
                maxLines = 1,
                color = StoreAppTheme.colors.textSecondary,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Composable
private fun PaymentData(
    paymentDataItems: List<PaymentDataItem>
) {
    Column {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.cart_payment_data),
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle1
            )
        }
        StoreAppTextField(
            value = paymentDataItems[0].value,
            onValueChange = paymentDataItems[0].valueChange,
            placeholder = paymentDataItems[0].placeholder,
            leadingIcon = Icons.Default.CreditCard
        )
        StoreAppTextField(
            value = paymentDataItems[1].value,
            onValueChange = paymentDataItems[1].valueChange,
            keyboardType = KeyboardType.Number,
            placeholder = paymentDataItems[1].placeholder,
            leadingIcon = Icons.Default.CreditCard
        )
        Row {
            StoreAppTextField(
                value = paymentDataItems[2].value,
                onValueChange = paymentDataItems[2].valueChange,
                placeholder = paymentDataItems[2].placeholder,
                leadingIcon = Icons.Default.DateRange,
                modifier = Modifier.weight(0.5f)
            )
            StoreAppTextField(
                value = paymentDataItems[3].value,
                onValueChange = paymentDataItems[3].valueChange,
                placeholder = paymentDataItems[3].placeholder,
                keyboardType = KeyboardType.Number,
                leadingIcon = Icons.Default.Password,
                modifier = Modifier.weight(0.5f)
            )
        }
    }
}

@Composable
fun GetPaymentDataItems(): List<PaymentDataItem> {
    var cardName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var cardExpireDate by remember { mutableStateOf("") }
    var cardCVC by remember { mutableStateOf("") }

    return listOf(
        PaymentDataItem(
            value = cardName,
            valueChange = { newValue -> cardName = newValue },
            placeholder = stringResource(id = R.string.payment_data_card_name)
        ),
        PaymentDataItem(
            value = cardNumber,
            valueChange = { newValue -> if (newValue.length < 9) cardNumber = newValue },
            placeholder = stringResource(id = R.string.payment_data_card_number)
        ),
        PaymentDataItem(
            value = cardExpireDate,
            valueChange = { newValue -> if (newValue.length < 6) cardExpireDate = newValue },
            placeholder = stringResource(id = R.string.payment_data_card_expire_date)
        ),
        PaymentDataItem(
            value = cardCVC,
            valueChange = { newValue -> if (newValue.length < 4) cardCVC = newValue },
            placeholder = stringResource(id = R.string.payment_data_card_cvc)
        ),
    )
}

data class PaymentDataItem(
    val value: String,
    val valueChange: (String) -> Unit,
    var placeholder: String,
)

//@Preview("default", showSystemUi = true)
//@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun CartPreview() {
//    StoreAppTheme {
//        PaymentProductItem(
//            cartProduct = CartProduct(name = "Gygabyte GeForce RTX 3080", price = 1234323)
//        )
//    }
//}