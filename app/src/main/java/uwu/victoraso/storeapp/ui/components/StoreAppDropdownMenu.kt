package uwu.victoraso.storeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.theme.Typography

@Composable
fun <T> StoreAppDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<T>,
    onItemClick: (item: T) -> Unit,
    onAddCartClick: (String) -> Unit,
    dismissOnItemClick: Boolean = true,
    itemText: @Composable (item: T) -> Unit,
    modifier: Modifier = Modifier
) {
    var addNewCartSelected by remember { mutableStateOf(false) }
    var newCartName by remember { mutableStateOf("") }

    Box(modifier = Modifier.padding(start = 32.dp)) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                onDismissRequest()
                addNewCartSelected = false
                newCartName = ""
            },
            modifier = modifier.background(StoreAppTheme.colors.uiBackground)
        ) {
            DropdownMenuItem(
                text = {
                    if (!addNewCartSelected) {
                        DropdownMenuItemText(stringResource(id = R.string.add_cart))
                    } else {
                        DropdownMenuItemTextField(
                            value = newCartName,
                            onValueChange = { newCartName = it },
                            onAddCartClick = onAddCartClick
                        )
                    }
                },
                onClick = {
                    addNewCartSelected = !addNewCartSelected
                },
                modifier = Modifier.height(40.dp)
            )
            items.forEach { item ->
                DropdownMenuItem(
                    text = { itemText(item) },
                    onClick = {
                        onItemClick(item)
                        if (dismissOnItemClick) {
                            onDismissRequest()
                            addNewCartSelected = false
                            newCartName = ""
                        }
                    },
                    modifier = Modifier.height(40.dp)
                )
            }
        }
    }
}

@Composable
private fun DropdownMenuItemText(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text, color = StoreAppTheme.colors.textLink,
            modifier = Modifier
                .padding(end = 8.dp)
                .weight(0.8f),
        )
        Icon(
            modifier = modifier
                .size(14.dp)
                .weight(0.2f),
            imageVector = Icons.Default.Add,
            contentDescription = "Add new cart"
        )
    }
}

@Composable
private fun DropdownMenuItemTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onAddCartClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    Row(verticalAlignment = Alignment.CenterVertically) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .padding(end = 8.dp)
                .weight(0.8f)
                .focusRequester(focusRequester),
            maxLines = 1,
            textStyle = Typography.body1,
        )
        IconButton(
            onClick = { onAddCartClick(value) },
            modifier = Modifier.weight(0.2f),
            enabled = value.isNotEmpty()
        ) {
            Icon(
                modifier = modifier.size(14.dp),
                imageVector = Icons.Default.Save,
                contentDescription = "Save new cart",
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
}