package uwu.victoraso.storeapp.ui.log.login

import StoreAppTextButton
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
import uwu.victoraso.storeapp.MainDestinations
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.ui.components.*
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

@Composable
fun LoginScreen(
    openAndPopUp: (String, String) -> Unit,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState

    StoreAppSurface(modifier = modifier.fillMaxSize()) {
        Box {
            StoreAppTopBar(screenTitle = "Welcome!")
            LoginScreenContent(
                openAndPopUp = openAndPopUp,
                onNavigateTo = onNavigateTo,
                uiState = uiState,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun LoginScreenContent(
    openAndPopUp: (String, String) -> Unit,
    onNavigateTo: (String) -> Unit,
    uiState: LoginUiState,
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.Center, modifier = modifier.fillMaxSize()) {
        Spacer(
            Modifier.windowInsetsTopHeight(
                WindowInsets.statusBars.add(WindowInsets(top = 56.dp))
            )
        )
        StoreAppCard(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            color = StoreAppTheme.colors.brand
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /** Email TextField **/
                StoreAppEmailTextField(
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
                /** Password TextField **/
                StoreAppPasswordTextField(
                    value = uiState.password,
                    onValueChange = viewModel::onPasswordChange,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StoreAppTextButton(
                        text = stringResource(id = R.string.sign_up),
                        onClick = { onNavigateTo(MainDestinations.SIGNUP_ROUTE) },
                    )
                    StoreAppTextButton(
                        text = stringResource(id = R.string.forgot_password),
                        onClick = { viewModel.onForgotPasswordClick() },
                    )
                }

                /** Login Button **/
                StoreAppButton(
                    onClick = { viewModel.onSignInClick(openAndPopUp) },
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
                ) {
                    Text(text = stringResource(R.string.sign_in))
                }
            }
        }
    }
}

