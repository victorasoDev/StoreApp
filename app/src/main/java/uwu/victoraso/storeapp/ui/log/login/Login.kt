package uwu.victoraso.storeapp.ui.log.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uwu.victoraso.storeapp.ui.components.StoreAppCard
import uwu.victoraso.storeapp.ui.components.StoreAppSurface
import uwu.victoraso.storeapp.ui.components.StoreAppTopBar
import uwu.victoraso.storeapp.ui.productcreate.StoreAppTextField
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

@Composable
fun LoginScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {


    StoreAppSurface(modifier = modifier.fillMaxSize()) {
        Box {
            StoreAppTopBar(screenTitle = "Welcome!")
        }
    }
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier
) {
    StoreAppCard(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        color = StoreAppTheme.colors.brand
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /** Email TextField **/
            StoreAppTextField(
                placeholder = "Email",
                name = "",
                onValueChange = { },
                modifier = Modifier.padding(vertical = 24.dp)
            )
            /** Password TextField **/
            StoreAppTextField(
                placeholder = "Email",
                name = "tagline",
                keyboardType = KeyboardType.Password,
                onValueChange = { },
                modifier = Modifier.padding(vertical = 24.dp)
            )
        }
    }
}

