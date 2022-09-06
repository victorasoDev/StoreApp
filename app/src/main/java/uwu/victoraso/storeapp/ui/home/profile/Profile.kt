package uwu.victoraso.storeapp.ui.home.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import uwu.victoraso.storeapp.ui.components.StoreAppButton
import uwu.victoraso.storeapp.ui.components.StoreAppSurface
import uwu.victoraso.storeapp.ui.productcreate.StoreAppTextField

@Composable
fun Profile(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel
) {
    var text by remember { mutableStateOf("") }
    var textSaved by remember { mutableStateOf("") }

    StoreAppSurface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            StoreAppTextField(placeholder = "datastore", name = text, onValueChange = { text = it })

            StoreAppButton(
                onClick = {
                    viewModel.setAdress(text)
                },
            modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Save Adress")
            }

            StoreAppButton(
                onClick = {
                    textSaved = viewModel.state.value.adress
                },
            modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Adress is $textSaved")
            }
        }
    }
}