package uwu.victoraso.storeapp.ui.log.login

import StoreAppTextButton
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uwu.victoraso.storeapp.MainDestinations
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.ui.components.*
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun LoginScreen(
    onClearAndNavigate: (String) -> Unit,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val screenUiState: LoginScreenUiState by viewModel.loginUiState.collectAsStateWithLifecycle()
    Log.d(DEBUG_TAG, "Recomposition")

    StoreAppSurface(modifier = modifier.fillMaxSize()) {
        Box {
            StoreAppTopBar(screenTitle = "Welcome!")
            when (screenUiState.loginUiState) {
                is CredentialsUiState.Success -> {
                    LoginScreenContent(
                        onClearAndNavigate = onClearAndNavigate,
                        onNavigateTo = onNavigateTo,
                        screenUiState = screenUiState,
                        viewModel = viewModel
                    )
                }
                is CredentialsUiState.Loading -> CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun LoginScreenContent(
    onClearAndNavigate: (String) -> Unit,
    onNavigateTo: (String) -> Unit,
    screenUiState: LoginScreenUiState,
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    val signInState by remember { viewModel.signInState }

    if (screenUiState.loginUiState is CredentialsUiState.Success) {
        email = screenUiState.loginUiState.email
        password = screenUiState.loginUiState.password
        rememberMe = screenUiState.loginUiState.rememberMe
    }

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
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.padding(vertical = 24.dp)
                )
                /** Password TextField **/
                StoreAppPasswordTextField(
                    value = password,
                    onValueChange = { password = it },
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
                        onClick = { viewModel.onForgotPasswordClick(LoginUiFields(email, password)) },
                    )
                }
                Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })

                /** Login Button **/


                StoreAppButton(
                    onClick = { viewModel.onSignInClick(onClearAndNavigate, LoginUiFields(email, password), rememberMe) },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp),
                    color = if (signInState) loadingButtonState() else Color.Transparent,
                    //TODO: deshabilitar el botón mientras se hace la petición
                ) {
                    Text(text = stringResource(R.string.sign_in))
                }
            }
        }
    }
}

