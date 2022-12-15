package uwu.victoraso.storeapp.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.Cart
import uwu.victoraso.storeapp.ui.components.StoreAppDivider
import uwu.victoraso.storeapp.ui.theme.AlphaNearOpaque
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

@Composable
fun DestinationBar(
    modifier: Modifier = Modifier,
    title: String = "Welcome to StoreApp",
    imageVector: ImageVector = Icons.Outlined.ExpandMore,
    destinationBarButtonVisible: Boolean = true,
    onDestinationBarButtonClick: () -> Unit
) {
    val textPadding = if (destinationBarButtonVisible) 45.dp else 0.dp

    Column(modifier = modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = StoreAppTheme.colors.uiBackground.copy(alpha = AlphaNearOpaque),
            contentColor = StoreAppTheme.colors.textSecondary,
            elevation = 0.dp
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1,
                color = StoreAppTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .align(CenterVertically)
                    .padding(textPadding)
            )
            if (destinationBarButtonVisible) DestinationBarIconButton(onDestinationBarButtonClick, imageVector, modifier.align(CenterVertically))
        }
        StoreAppDivider()
    }
}

@Composable
fun DestinationBarWithLogo(
    modifier: Modifier = Modifier,
    onLogoButtonClick: () -> Unit
) {
    Column(modifier = modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = StoreAppTheme.colors.uiBackground.copy(alpha = AlphaNearOpaque),
            contentColor = StoreAppTheme.colors.textSecondary,
            elevation = 0.dp
        ) {
            Icon(
                painter = painterResource(id = R.drawable.g_k_store_logo),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .align(CenterVertically)
                    .padding(vertical = 8.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onLogoButtonClick()
                    }
            )
        }
        StoreAppDivider()
    }
}

@Composable
fun DestinationBar(
    modifier: Modifier = Modifier,
    title: String,
    cart: Cart,
    onTitleValueChange: (String) -> Unit,
    imageVector: ImageVector = Icons.Outlined.ExpandMore,
    onDestinationBarButtonClick: () -> Unit,
    onChangeCartNameClick: (String) -> Unit,
    onDeleteCartClick: (Cart) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var nameTextFieldVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = StoreAppTheme.colors.uiBackground.copy(alpha = AlphaNearOpaque),
            contentColor = StoreAppTheme.colors.textSecondary,
            elevation = 0.dp
        ) {
            if (!nameTextFieldVisible) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle1,
                    color = StoreAppTheme.colors.textSecondary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .align(CenterVertically)
                        .padding(start = 45.dp)
                        .clickable {
                            nameTextFieldVisible = true
                        }
                )
                DestinationBarIconButton(onDestinationBarButtonClick, imageVector, modifier.align(CenterVertically))
            } else {
                BasicTextField(
                    value = title,
                    onValueChange = onTitleValueChange,
                    textStyle = MaterialTheme.typography.subtitle1.copy(
                        textAlign = TextAlign.Center,
                        color = StoreAppTheme.colors.textSecondary
                    ),
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .align(CenterVertically)
                        .padding(start = 45.dp)
                        .focusRequester(focusRequester)
                        .onFocusEvent {
                            if (!it.isFocused) {
                                onChangeCartNameClick(title)
                                focusManager.clearFocus()
                            }
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        nameTextFieldVisible = false
                    })
                )
                LaunchedEffect(key1 = Unit) { focusRequester.requestFocus() }
                DestinationBarIconButton(
                    newCartName = title,
                    onDestinationBarButtonClick = {
                        onChangeCartNameClick(it)
                        focusManager.clearFocus()
                        nameTextFieldVisible = false
                    },
                    imageVector = Icons.Outlined.Save,
                    modifier = modifier.align(CenterVertically)
                )
                DestinationBarIconButton(
                    newCartName = title,
                    onDestinationBarButtonClick = {
                        onDeleteCartClick(cart)
                        focusManager.clearFocus()
                        nameTextFieldVisible = false
                    },
                    imageVector = Icons.Outlined.Delete,
                    modifier = modifier.align(CenterVertically)
                )
            }
        }
    }
    StoreAppDivider()
}


@Composable
private fun DestinationBarIconButton(
    newCartName: String,
    onDestinationBarButtonClick: (String) -> Unit,
    imageVector: ImageVector,
    modifier: Modifier
) {
    IconButton(
        onClick = { onDestinationBarButtonClick(newCartName) },
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            tint = StoreAppTheme.colors.brand,
            contentDescription = null
        )
    }
}


@Composable
private fun DestinationBarIconButton(
    onDestinationBarButtonClick: () -> Unit,
    imageVector: ImageVector,
    modifier: Modifier
) {
    IconButton(
        onClick = { onDestinationBarButtonClick() },
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            tint = StoreAppTheme.colors.brand,
            contentDescription = null
        )
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun PreviewDestinationBar() {
    StoreAppTheme {
        DestinationBar(onDestinationBarButtonClick = { })
    }
}