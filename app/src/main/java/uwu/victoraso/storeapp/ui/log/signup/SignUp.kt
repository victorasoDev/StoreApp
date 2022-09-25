package uwu.victoraso.storeapp.ui.log.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uwu.victoraso.storeapp.ui.components.StoreAppButton
import uwu.victoraso.storeapp.ui.components.StoreAppCard
import uwu.victoraso.storeapp.ui.components.StoreAppSurface
import uwu.victoraso.storeapp.ui.components.StoreAppTopBar
import uwu.victoraso.storeapp.ui.productcreate.StoreAppTextField
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

@Composable
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState

    StoreAppSurface(modifier = modifier.fillMaxSize()) {
        Box {
            StoreAppTopBar(screenTitle = "Welcome!")
            SignUpScreenContent(
                openAndPopUp = openAndPopUp,
                uiState = uiState,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun SignUpScreenContent(
    openAndPopUp: (String, String) -> Unit,
    uiState: SignUpUiState,
    viewModel: SignUpViewModel,
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
                name = uiState.email,
                onValueChange = viewModel::onEmailChange,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            /** Password TextField **/
            StoreAppTextField(
                placeholder = "Password",
                name = uiState.password,
                keyboardType = KeyboardType.Password,
                onValueChange = viewModel::onPasswordChange,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            /** Repeat-Password TextField **/
            StoreAppTextField(
                placeholder = "Repeat-Password",
                name = uiState.repeatPassword,
                keyboardType = KeyboardType.Password,
                onValueChange = viewModel::onRepeatPasswordChange,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            /** Sign Up Button **/
            StoreAppButton(
                onClick = { viewModel.onSignUpClick(openAndPopUp) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(uwu.victoraso.storeapp.R.string.create_account))
            }
        }
    }
}