package uwu.victoraso.storeapp.ui.home.profile.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uwu.victoraso.storeapp.repositories.userpreferences.UserPreferencesRepository
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG_WISHLIST
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    val settingsUiState: StateFlow<SettingsUiState> =
        userPreferencesRepository.darkThemeConfig
            .map {
                SettingsUiState.Success(
                    settings = UserEditableSettings(
                        darkThemeConfig = it
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = SettingsUiState.Loading
            )

    fun updateDarkThemeConfig(darkThemeConfig: Boolean) {
        viewModelScope.launch {
            Log.d(DEBUG_TAG_WISHLIST, darkThemeConfig.toString())
            userPreferencesRepository.setDarkThemeConfig(darkThemeConfig)
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