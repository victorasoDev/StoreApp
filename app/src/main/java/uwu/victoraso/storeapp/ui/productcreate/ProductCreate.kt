package uwu.victoraso.storeapp.ui.productcreate

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.ProductCollection
import uwu.victoraso.storeapp.model.ProductRepo
import uwu.victoraso.storeapp.ui.components.*
import uwu.victoraso.storeapp.ui.theme.Neutral8
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.formatPrice
import uwu.victoraso.storeapp.ui.utils.mirroringBackIcon

private val MinTitleOffset = 56.dp

@Composable
fun ProductCreate(
    upPress: () -> Unit,
    addNewProduct: (/*name*/String, /*price*/String, /*tagline*/String, /*categories*/List<String>) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val scroll = rememberScrollState(0)

        Header()
        Body(scroll, addNewProduct)
        Up(upPress)
    }
}

@Composable
private fun Header() {
    Spacer(
        modifier = Modifier
            .height(55.dp)
            .fillMaxWidth()
            .background(Brush.horizontalGradient(StoreAppTheme.colors.tornado1))
    )
}

@Composable
private fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = Neutral8.copy(alpha = 0.32f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = mirroringBackIcon(),
            tint = StoreAppTheme.colors.iconInteractive,
            contentDescription = null
        )
    }
}

@Composable
private fun Body(
    scroll: ScrollState,
    addNewProduct: (/*name*/String, /*price*/String, /*tagline*/String, /*categories*/List<String>) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var tagline by remember { mutableStateOf("") }
    var categories = mutableStateListOf("Cat1", "Cat2", "Cat3")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .verticalScroll(scroll)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
                .height(MinTitleOffset)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        StoreAppTextField(
            placeholder = "Name",
            name = name,
            onValueChange = { name = it }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        StoreAppTextField(
            placeholder = "Price",
            keyboardType = KeyboardType.Number,
            name = price,
            onValueChange = { price = it }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        StoreAppTextField(
            placeholder = "Tagline",
            name = tagline,
            onValueChange = { tagline = it }
        )
        StoreAppButton(
            onClick = { addNewProduct(name, price, tagline, categories) },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Save Product")
        }
    }
}

@Composable
fun StoreAppTextField(
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    name: String,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        value = name,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        placeholder = { Text(text = placeholder) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = StoreAppTheme.colors.textHelp,
            backgroundColor = StoreAppTheme.colors.textInteractive,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}