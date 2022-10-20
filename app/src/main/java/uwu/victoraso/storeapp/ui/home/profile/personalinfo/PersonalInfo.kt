package uwu.victoraso.storeapp.ui.home.profile.personalinfo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.UserProfile
import uwu.victoraso.storeapp.ui.components.StoreAppDialog
import uwu.victoraso.storeapp.ui.components.StoreAppDialogButton
import uwu.victoraso.storeapp.ui.components.StoreAppDialogTitle
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun PersonalInfoDialog(
    show: Boolean,
    viewModel: PersonalInfoViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    val personalInfoUiState: PersonalInfoUiState by viewModel.personalInfoUiState.collectAsStateWithLifecycle()

    if (show && personalInfoUiState is PersonalInfoUiState.Success) {

        val list = GetPersonalInfoItems(
            userProfile = (personalInfoUiState as PersonalInfoUiState.Success).userProfile,
            viewModel = viewModel
        )

        StoreAppDialog(onDismiss = onDismiss, properties = DialogProperties(dismissOnClickOutside = true)) {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
            ) {
                item {
                    StoreAppDialogTitle(stringID = R.string.user_profile_personal_info)
                }
                items(list) { item ->
                    PersonalInfoItem(item = item)
                }
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        StoreAppDialogButton(onClick = onDismiss, stringID = R.string.close)
                    }
                }
            }
        }
    }
}

@Composable
fun PersonalInfoItem(item: PersonalInfoItem) {
    Text(
        text = item.title,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.subtitle1
    )
    if (item.onValueEdit) {
        PersonalInfoTextField(item = item)
    } else {
        PersonalInfoText(item = item)
    }
}

@Composable
fun PersonalInfoText(item: PersonalInfoItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.value.ifEmpty { stringResource(id = uwu.victoraso.storeapp.R.string.user_data_not_provided, item.title.lowercase()) },
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .weight(0.8f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle1,
            color = StoreAppTheme.colors.textSecondary
        )
        IconButton(
            onClick = { item.onValueEditChange(true) },
            modifier = Modifier.weight(0.2f),
        ) {
            Icon(
                modifier = Modifier.size(14.dp),
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit ${item.title}",
            )
        }
    }
}

@Composable
fun PersonalInfoTextField(item: PersonalInfoItem) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = item.value,
            onValueChange = item.valueChange,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .weight(0.8f)
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                item.saveData(item.value)
                item.onValueEditChange(false)
                focusManager.clearFocus()
            }),
            maxLines = 1,
            textStyle = MaterialTheme.typography.subtitle1,
        )
        IconButton(
            onClick = {
                item.saveData(item.value)
                item.onValueEditChange(false)
            },
            modifier = Modifier.weight(0.2f),
        ) {
            Icon(
                modifier = Modifier.size(14.dp),
                imageVector = Icons.Default.Save,
                contentDescription = "Edit ${item.title}",
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun GetPersonalInfoItems(
    userProfile: UserProfile,
    viewModel: PersonalInfoViewModel
): List<PersonalInfoItem> {
    var name by remember { mutableStateOf(userProfile.name) }
    var onNameChange by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf(userProfile.email) }
    var onEmailChange by remember { mutableStateOf(false) }
    var adress by remember { mutableStateOf(userProfile.adress) }
    var onAdressChange by remember { mutableStateOf(false) }
    var phone by remember { mutableStateOf(userProfile.phone) }
    var onPhoneChange by remember { mutableStateOf(false) }
    val list = listOf(
        PersonalInfoItem(
            title = stringResource(id = R.string.user_name_label),
            value = name,
            valueChange = { newName -> name = newName },
            onValueEdit = onNameChange,
            onValueEditChange = { value ->
                onNameChange = value
                onEmailChange = false
                onAdressChange = false
                onPhoneChange = false
            },
            saveData = viewModel::updateName
        ),
        PersonalInfoItem(
            title = stringResource(id = R.string.user_email_label),
            value = email,
            valueChange = { newEmail -> email = newEmail },
            onValueEdit = onEmailChange,
            onValueEditChange = { value ->
                onEmailChange = value
                onNameChange = false
                onAdressChange = false
                onPhoneChange = false
            },
            saveData = viewModel::updateEmail
        ),
        PersonalInfoItem(
            title = stringResource(id = R.string.user_adress_label),
            value = adress,
            valueChange = { newAdress -> adress = newAdress },
            onValueEdit = onAdressChange,
            onValueEditChange = { value ->
                onAdressChange = value
                onEmailChange = false
                onNameChange = false
                onPhoneChange = false
            },
            saveData = viewModel::updateAdress
        ),
        PersonalInfoItem(
            title = stringResource(id = R.string.user_phone_label),
            value = phone,
            valueChange = { newPhone -> phone = newPhone },
            onValueEdit = onPhoneChange,
            onValueEditChange = { value ->
                onPhoneChange = value
                onEmailChange = false
                onAdressChange = false
                onNameChange = false
            },
            saveData = viewModel::updatePhone
        )
    )
    return list
}

data class PersonalInfoItem(
    val title: String,
    var value: String,
    val valueChange: (String) -> Unit,
    val onValueEdit: Boolean,
    val onValueEditChange: (Boolean) -> Unit,
    val saveData: (String) -> Unit
)