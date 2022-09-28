package uwu.victoraso.storeapp.ui.log.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uwu.victoraso.storeapp.MainDestinations
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.SnackbarManager
import uwu.victoraso.storeapp.model.service.AccountService
import uwu.victoraso.storeapp.model.service.StorageService
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.repositories.userpreferences.UserPreferencesRepository
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import uwu.victoraso.storeapp.ui.utils.isValidEmail
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val accountService: AccountService,
    private val storageService: StorageService,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    var loginUiState: StateFlow<LoginScreenUiState> = loginUiStateStream(
        userDataRepository = userPreferencesRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LoginScreenUiState.Loading
        )

    fun onSignInClick(onClearAndNavigate: (String) -> Unit, loginUiFields: LoginUiFields) {
        if (!loginUiFields.email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (loginUiFields.password.isBlank()) {
            SnackbarManager.showMessage(R.string.empty_password_error)
            return
        }

        viewModelScope.launch {
            val oldUserId = accountService.getUserId()
            accountService.authenticate(loginUiFields.email, loginUiFields.password) { error ->
                if (error == null) {
                    linkWithEmail(loginUiFields)
                    updateUserId(oldUserId, onClearAndNavigate)
                    setUserEmail(loginUiFields.email)
                } else Log.d(DEBUG_TAG, error.toString())
            }
        }
    }

    private fun linkWithEmail(loginUiFields: LoginUiFields) {
        viewModelScope.launch {
            accountService.linkAccount(loginUiFields.email, loginUiFields.password) { error ->
                if (error != null) Log.d(DEBUG_TAG, error.toString())
            }
        }
    }

    private fun setUserEmail(email: String) { viewModelScope.launch { userPreferencesRepository.setUserEmail(email) } }

    private fun updateUserId(oldUserId: String, onClearAndNavigate: (String) -> Unit) {
        viewModelScope.launch {
            val newUserId = accountService.getUserId()
            storageService.updateUserId(oldUserId, newUserId) { error ->
                if (error != null) Log.d(DEBUG_TAG, error.toString())
                else onClearAndNavigate(MainDestinations.HOME_ROUTE)
            }
        }
    }

    fun onForgotPasswordClick(loginUiFields: LoginUiFields) {
        if (!loginUiFields.email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        viewModelScope.launch {
            accountService.sendRecoveryEmail(loginUiFields.email) { error ->
                if (error != null) Log.d(DEBUG_TAG, error.toString())
                else SnackbarManager.showMessage(R.string.recovery_email_sent)
            }
        }
    }
}

data class LoginUiFields(
    val email: String = "",
    val password: String = ""
)

private fun loginUiStateStream(
    userDataRepository: UserPreferencesRepository,
): Flow<LoginScreenUiState> {
    val userEmail: Flow<String> = userDataRepository.getUserEmail

    return userEmail
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> {
                    LoginScreenUiState.Success(
                        userEmail.firstOrNull() ?: "" //TODO
                    )
                }
                is Result.Loading -> {
                    LoginScreenUiState.Loading
                }
                is Result.Error -> {
                    LoginScreenUiState.Error
                }
            }
        }
}

sealed interface LoginScreenUiState {
    data class Success(val email: String) : LoginScreenUiState
    object Error : LoginScreenUiState
    object Loading : LoginScreenUiState
}