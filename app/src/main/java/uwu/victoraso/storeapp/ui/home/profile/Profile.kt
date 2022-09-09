package uwu.victoraso.storeapp.ui.home.profile

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import uwu.victoraso.storeapp.model.UserProfile
import uwu.victoraso.storeapp.ui.components.StoreAppButton
import uwu.victoraso.storeapp.ui.components.StoreAppSurface
import uwu.victoraso.storeapp.ui.productcreate.StoreAppTextField
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun Profile(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel
) {
    Log.d(DEBUG_TAG, "antes de profileUiState")
    val profileUiState: ProfileUiState by viewModel.profileUiState.collectAsStateWithLifecycle()

    StoreAppSurface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            when(profileUiState) {
                ProfileUiState.Loading -> {
                    Log.d(DEBUG_TAG,  "Loading")
                }
                ProfileUiState.Error -> {
                    Log.d(DEBUG_TAG,  "Error")
                }
                is ProfileUiState.Success -> {
                    Log.d(DEBUG_TAG,  "Success")
                    ProfileTest(
                        viewModel = viewModel,
                        userProfile = (profileUiState as ProfileUiState.Success).userProfile
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileTest(
    viewModel: ProfileViewModel,
    userProfile: UserProfile
) {
    var text by remember { mutableStateOf("") }

    Column {
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
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
            onClick = { },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Adress is ${userProfile.adress}")
        }
    }
}