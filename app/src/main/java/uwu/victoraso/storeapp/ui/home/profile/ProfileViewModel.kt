package uwu.victoraso.storeapp.ui.home.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uwu.victoraso.storeapp.repositories.userpreferences.UserPreferencesRepository
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.model.UserProfile
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel()
{
    var profileUiState: StateFlow<ProfileUiState> = profileUiStateStream(
        userDataRepository = userPreferencesRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState.Loading
        )

    fun setAdress(adress: String) {
        viewModelScope.launch {
            userPreferencesRepository.setUserAdress(adress)
        }
    }
}

private fun profileUiStateStream(
    userDataRepository: UserPreferencesRepository,
): Flow<ProfileUiState> {
    val userProfile: Flow<UserProfile> = userDataRepository.getUserProfile

    return userProfile
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> {
                    ProfileUiState.Success(
                        userProfile.first()
                    )
                }
                is Result.Loading -> {
                    ProfileUiState.Loading
                }
                is Result.Error -> {
                    ProfileUiState.Error
                }
            }
        }
}

sealed interface ProfileUiState {
    data class Success(val userProfile: UserProfile) : ProfileUiState
    object Error : ProfileUiState
    object Loading : ProfileUiState
}