package uwu.victoraso.storeapp.ui.home.profile.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.ui.components.StoreAppDialog
import uwu.victoraso.storeapp.ui.components.StoreAppDialogButton
import uwu.victoraso.storeapp.ui.components.StoreAppDialogTitle

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SettingsDialog(
    show: Boolean,
    viewModel: SettingsViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {

    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    if (show) {
        SettingsDialog(
            settingsUiState = settingsUiState,
            onDismiss = onDismiss,
            onChangeDarkThemeConfig = viewModel::updateDarkThemeConfig
        )
    }
}

@Composable
fun SettingsDialog(
    settingsUiState: SettingsUiState,
    onDismiss: () -> Unit,
    onChangeDarkThemeConfig: (Boolean) -> Unit
) {
    StoreAppDialog(onDismiss = onDismiss, properties = DialogProperties(dismissOnClickOutside = true)) {
        Column(
            Modifier
                .fillMaxWidth()
        ) {
            StoreAppDialogTitle(stringID = R.string.user_profile_personal_info)
            SettingsPanel(settingsUiState = settingsUiState, onChangeDarkThemeConfig = onChangeDarkThemeConfig)
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                StoreAppDialogButton(onClick = onDismiss, stringID = R.string.close)
            }
        }
    }
}

@Composable
private fun SettingsPanel(
    settingsUiState: SettingsUiState,
    onChangeDarkThemeConfig: (Boolean) -> Unit
) {
    when (settingsUiState) {
        SettingsUiState.Loading -> {
            CircularProgressIndicator()
        }
        is SettingsUiState.Success -> {
            SettingsDialogSectionTitle(text = stringResource(id = R.string.user_profile_dark_mode_preference))
            Column (Modifier.selectableGroup()) {
                SettingsDialogThemeChooserRow(
                    text = stringResource(R.string.dark_mode_config_light),
                    selected = !settingsUiState.settings.darkThemeConfig,
                    onClick = { onChangeDarkThemeConfig(false) }
                )
                SettingsDialogThemeChooserRow(
                    text = stringResource(R.string.dark_mode_config_dark),
                    selected = settingsUiState.settings.darkThemeConfig,
                    onClick = { onChangeDarkThemeConfig(true) }
                )
            }
        }
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}