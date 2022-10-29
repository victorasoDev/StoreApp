package uwu.victoraso.storeapp.ui.home.profile.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.repositories.userpreferences.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val darkThemeConfigStream: Flow<Result<Boolean>> = userPreferencesRepository.getDarkMode.asResult()

    val settingsUiState: StateFlow<SettingsUiState> =
        userPreferencesRepository.getDarkMode
            .map {
                SettingsUiState.Success(
                    settings = UserEditableSettings(
                        darkThemeConfig = it
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                // Starting eagerly means the user data is ready when the SettingsDialog is laid out
                // for the first time. Without this, due to b/221643630 the layout is done using the
                // "Loading" text, then replaced with the user editable fields once loaded, however,
                // the layout height doesn't change meaning all the fields are squashed into a small
                // scrollable column.
                // TODO: Change to SharingStarted.WhileSubscribed(5_000) when b/221643630 is fixed
                started = SharingStarted.Eagerly,
                initialValue = SettingsUiState.Loading
            )

    fun updateDarkThemeConfig(darkThemeConfig: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setDarkMode(darkThemeConfig)
        }
    }
}

/**
 * Represents the settings which the user can edit within the app.
 */
data class UserEditableSettings(val darkThemeConfig: Boolean)

sealed interface SettingsUiState {
    object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}