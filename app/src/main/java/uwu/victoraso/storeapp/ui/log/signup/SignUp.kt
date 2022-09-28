package uwu.victoraso.storeapp.ui.log.signup

import StoreAppTextButton
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uwu.victoraso.storeapp.MainDestinations
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.ui.components.*
import uwu.victoraso.storeapp.ui.log.login.LoginScreenUiState
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

@Composable
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {

    StoreAppSurface(modifier = modifier.fillMaxSize()) {
        Box {
            StoreAppTopBar(screenTitle = "Welcome!")
            SignUpScreenContent(
                openAndPopUp = openAndPopUp,
                viewModel = viewModel,
                onNavigateTo = onNavigateTo
            )
        }
    }
}

@Composable
fun SignUpScreenContent(
    openAndPopUp: (String, String) -> Unit,
    onNavigateTo: (String) -> Unit,
    viewModel: SignUpViewModel,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.Center, modifier = modifier.fillMaxSize()) {
        Spacer(
            Modifier.windowInsetsTopHeight(
                WindowInsets.statusBars.add(WindowInsets(top = 56.dp))
            )
        )
        StoreAppCard(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            color = StoreAppTheme.colors.brand
        ) {
            Column(
                modifier = modifier
                    .padding(vertical = 24.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /** Email TextField **/
                StoreAppEmailTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                /** Password TextField **/
                StoreAppPasswordTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                /** Repeat-Password TextField **/
                StoreAppRepeatPasswordTextField(
                    value = repeatPassword,
                    onValueChange = { repeatPassword = it },
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    StoreAppTextButton(
                        text = stringResource(id = R.string.sign_in),
                        onClick = { onNavigateTo(MainDestinations.LOGIN_ROUTE) },
                    )
                }
                /** Sign Up Button **/
                StoreAppButton(
                    onClick = { viewModel.onSignUpClick(openAndPopUp, SignUpUiFields(email, password, repeatPassword)) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = stringResource(R.string.create_account))
                }
            }
        }
    }
}