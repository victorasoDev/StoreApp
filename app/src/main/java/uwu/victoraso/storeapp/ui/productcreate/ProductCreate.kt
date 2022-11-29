package uwu.victoraso.storeapp.ui.productcreate

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import uwu.victoraso.storeapp.ui.components.StoreAppButton
import uwu.victoraso.storeapp.ui.components.StoreAppTopBar


@Composable
fun ProductCreate(
    upPress: () -> Unit,
    modifier: Modifier = Modifier,
    addNewProduct: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        val scroll = rememberScrollState(0)

        Body(scroll, addNewProduct,  modifier = modifier.align(Alignment.Center))
        StoreAppTopBar(upPress = upPress, screenTitle = "Product creation")
    }
}

@Composable
private fun Body(
    scroll: ScrollState,
    addNewProduct: () -> Unit,
    modifier: Modifier
) {
    StoreAppButton(
        modifier = modifier,
        onClick = {
            addNewProduct()
        },
    ) {
        Text(text = "Save Product")
    }
}