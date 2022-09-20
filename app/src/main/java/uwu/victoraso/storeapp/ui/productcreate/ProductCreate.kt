package uwu.victoraso.storeapp.ui.productcreate

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.Filter
import uwu.victoraso.storeapp.model.StoreAppFilters
import uwu.victoraso.storeapp.ui.components.*
import uwu.victoraso.storeapp.ui.theme.Neutral8
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import uwu.victoraso.storeapp.ui.utils.mirroringBackIcon

private val MinTitleOffset = 56.dp

@Composable
fun ProductCreate(
    upPress: () -> Unit,
    modifier: Modifier = Modifier,
    addNewProduct: (/*name*/String, /*price*/String, /*tagline*/String, /*categories*/List<String>) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        val scroll = rememberScrollState(0)

        Body(scroll, addNewProduct)
        StoreAppTopBar(upPress = upPress, screenTitle = "Product creation")
    }
}

@Composable
private fun Body(
    scroll: ScrollState,
    addNewProduct: (/*name*/String, /*price*/String, /*tagline*/String, /*categories*/List<String>) -> Unit
) {
    // con el by accedes directamente al value, así no tienes por qué llamar a name.value (solo a name)
    var name by rememberSaveable { mutableStateOf("") } //rememberSaveable te guarda el estado aunque rotes la pantalla
    var price by remember { mutableStateOf("") }
    var tagline by remember { mutableStateOf("") }
    var categories by remember { mutableStateOf(ArrayList<String>()) }
    val filters by remember { mutableStateOf(StoreAppFilters) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
        Spacer(modifier = Modifier.padding(4.dp))
        FilterBar(
            filters = filters,
            onShowFilter = { },
            addRemoveCategory = { selected, filterName ->
                if (selected) {
                    if (!categories.contains(filterName)) categories.add(filterName)
                } else {
                    if (categories.contains(filterName)) categories.remove(filterName)
                }
                Log.d(DEBUG_TAG, categories.toString())
            }
        )
        StoreAppButton(
            onClick = {
                addNewProduct(name, price, tagline, categories)
                //TODO: hay mejor forma de vaciar los campos?
                name = ""
                price = ""
                tagline = ""
                categories = ArrayList()
            },
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