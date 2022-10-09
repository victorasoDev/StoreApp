package uwu.victoraso.storeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

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
                addNewCartSelected = false
                newCartName = ""
                onDismissRequest()
            },
            modifier = modifier.background(StoreAppTheme.colors.uiBackground)
        ) {
            DropdownMenuItem(
                text = {
                    if (!addNewCartSelected) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Add new cart", color = StoreAppTheme.colors.textLink)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(modifier = Modifier.size(12.dp), imageVector = Icons.Default.Add, contentDescription = "Add new cart")
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextField(
                                value = newCartName,
                                onValueChange = { newCartName = it },
                                placeholder = { Text(text = "Cart name") },
                                modifier = Modifier.height(40.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clickable { onAddCartClick(newCartName) },
                                imageVector = Icons.Default.Save,
                                contentDescription = "Save new cart",
                            )
                        }
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
                        if (dismissOnItemClick) onDismissRequest()
                    },
                    modifier = Modifier.height(40.dp)
                )
            }
        }
    }
}