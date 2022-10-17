package uwu.victoraso.storeapp.ui.home.profile.personalinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uwu.victoraso.storeapp.model.UserProfile
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.repositories.userpreferences.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class PersonalInfoViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    var personalInfoUiState: StateFlow<PersonalInfoUiState> = personalInfoUiStateStream(
        userDataRepository = userPreferencesRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PersonalInfoUiState.Loading
        )


    private fun personalInfoUiStateStream(
        userDataRepository: UserPreferencesRepository
    ): Flow<PersonalInfoUiState> {
        val userProfile: Flow<UserProfile> = userDataRepository.getUserProfile

        return userProfile
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Success -> {
                        PersonalInfoUiState.Success(
                            userProfile.first()
                        )
                    }
                    is Result.Loading -> {
                        PersonalInfoUiState.Loading
                    }
                    is Result.Error -> {
                        PersonalInfoUiState.Error
                    }
                }
            }
    }

    fun updateName(name: String) {
        viewModelScope.launch { userPreferencesRepository.setUserName(name) }
    }

    fun updateEmail(email: String) {
        viewModelScope.launch { userPreferencesRepository.setUserEmail(email) }
    }

    fun updateAdress(adress: String) {
        viewModelScope.launch { userPreferencesRepository.setUserAdress(adress) }
    }

    fun updatePhone(phone: String) {
        viewModelScope.launch { userPreferencesRepository.setUserPhone(phone) }
    }
}

sealed interface PersonalInfoUiState {
    data class Success(val userProfile: UserProfile) : PersonalInfoUiState
    object Error : PersonalInfoUiState
    object Loading : PersonalInfoUiState
}