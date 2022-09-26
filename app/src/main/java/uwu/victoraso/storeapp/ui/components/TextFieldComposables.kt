package uwu.victoraso.storeapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

@Composable
fun StoreAppTextField( //TODO hacer lo mismo que con el textField de la pass
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    name: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    OutlinedTextField(
        value = name,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .padding(horizontal = 16.dp),
        placeholder = { Text(text = placeholder) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = StoreAppTheme.colors.textHelp,
            backgroundColor = StoreAppTheme.colors.uiFloated,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun StoreAppRepeatPasswordTextField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    StoreAppPasswordTextField(placeholder = "Repeat-Password", name = value, onValueChange = onValueChange, modifier = modifier)
}

@Composable
fun StoreAppPasswordTextField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    StoreAppPasswordTextField(placeholder = "Password", name = value, onValueChange = onValueChange, modifier = modifier)
}

@Composable
private fun StoreAppPasswordTextField(
    placeholder: String,
    name: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    val icon = if (isVisible) painterResource(uwu.victoraso.storeapp.R.drawable.ic_visibility_on)
    else painterResource(uwu.victoraso.storeapp.R.drawable.ic_visibility_off)

    val visualTransformation = if (isVisible) VisualTransformation.None
    else PasswordVisualTransformation()

    OutlinedTextField(
        value = name,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(text = placeholder) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .padding(horizontal = 16.dp),
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(painter = icon, contentDescription = "Visibility")
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = StoreAppTheme.colors.textHelp,
            backgroundColor = StoreAppTheme.colors.uiFloated,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}