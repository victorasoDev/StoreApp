package uwu.victoraso.storeapp.ui.log.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
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
import uwu.victoraso.storeapp.ui.utils.CLEAR_USER_PREFERENCE
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
) : ViewModel() {

    private val userEmailStream: Flow<Result<String>> = userPreferencesRepository.getUserEmail.asResult()
    private val userPasswordStream: Flow<Result<String>> = userPreferencesRepository.getUserPassword.asResult()
    private val rememberMeStream: Flow<Result<Boolean>> = userPreferencesRepository.getRememberMe.asResult()

    /** UiState when signInButton is clicked **/
    var signInState = mutableStateOf(false)

    var loginUiState: StateFlow<LoginScreenUiState> =
        combine(
            userEmailStream,
            userPasswordStream,
            rememberMeStream
        ) { userEmailResult, userPasswordResult, rememberMeResult ->
            val credentials: CredentialsUiState =
                when {
                    userEmailResult is Result.Success && userPasswordResult is Result.Success && rememberMeResult is Result.Success -> {
                        CredentialsUiState.Success(userEmailResult.data, userPasswordResult.data, rememberMeResult.data)
                    }
                    userEmailResult is Result.Loading -> { CredentialsUiState.Loading }
                    else -> CredentialsUiState.Success("", "", false)
                }
            LoginScreenUiState(credentials)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = LoginScreenUiState(loginUiState = CredentialsUiState.Loading)
            )

    fun onSignInClick(onClearAndNavigate: (String) -> Unit, loginUiFields: LoginUiFields, rememberMe: Boolean) {
        signInState.value = true
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
                    setRememberMe(rememberMe)
                    if (rememberMe) setUserPassword(loginUiFields.password) /**TODO: encryptar la pass**/
                    signInState.value = true
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

    private fun setUserEmail(email: String) {
        viewModelScope.launch { userPreferencesRepository.setUserEmail(email) }
    }

    private fun setUserPassword(password: String) {
        viewModelScope.launch { userPreferencesRepository.setUserPassword(password) }
    }

    private fun setRememberMe(rememberMe: Boolean) {
        viewModelScope.launch { userPreferencesRepository.setRememberMe(rememberMe) }
    }

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

sealed interface CredentialsUiState {
    data class Success(val email: String, val password: String, val rememberMe: Boolean) : CredentialsUiState
    object Loading : CredentialsUiState
}

data class LoginScreenUiState(
    val loginUiState: CredentialsUiState
)