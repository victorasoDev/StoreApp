package uwu.victoraso.storeapp.ui.log.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.ui.components.*
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

@Composable
fun LoginScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState

    StoreAppSurface(modifier = modifier.fillMaxSize()) {
        Box {
            StoreAppTopBar(screenTitle = "Welcome!")
            LoginScreenContent(
                openAndPopUp = openAndPopUp,
                uiState = uiState,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun LoginScreenContent(
    openAndPopUp: (String, String) -> Unit,
    uiState: LoginUiState,
    viewModel: LoginViewModel,
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
                modifier = Modifier.padding(vertical = 24.dp)
            )
            /** Password TextField **/
            StoreAppPasswordTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
            )
            /** Login Button **/
            StoreAppButton(
                onClick = { viewModel.onSignInClick(openAndPopUp) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(R.string.sign_in))
            }

            StoreAppButton(
                onClick = { viewModel.onForgotPasswordClick() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(R.string.forgot_password))
            }
        }
    }
}

