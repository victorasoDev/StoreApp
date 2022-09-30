package uwu.victoraso.storeapp.ui.log.login

import android.util.Log
import androidx.compose.runtime.MutableState
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
    private var _isSignInLoading = mutableStateOf(false)
    val isSignInLoading get() = _isSignInLoading

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
        _isSignInLoading.startLoading()
        if (!loginUiFields.email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            _isSignInLoading.stopLoading()
            return
        }

        if (loginUiFields.password.isBlank()) {
            SnackbarManager.showMessage(R.string.empty_password_error)
            _isSignInLoading.stopLoading()
            return
        }

        viewModelScope.launch {
            val oldUserId = accountService.getUserId()
            accountService.authenticate(loginUiFields.email, loginUiFields.password) { error ->
                if (error == null) {
                    linkWithEmail(loginUiFields)
                    updateUserId(oldUserId, onClearAndNavigate)
                    saveCredentials(loginUiFields.email, loginUiFields.password, rememberMe)
                } else Log.d(DEBUG_TAG, error.toString())
                _isSignInLoading.stopLoading()
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

    private fun saveCredentials(email: String, password: String, rememberMe: Boolean) {
        viewModelScope.launch { userPreferencesRepository.setUserEmail(email) }
        viewModelScope.launch { userPreferencesRepository.setRememberMe(rememberMe) }
        if (rememberMe) viewModelScope.launch { userPreferencesRepository.setUserPassword(password) }/**TODO: encryptar la pass**/
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

    //TODO: pasar a un viewModel padre?
    private fun MutableState<Boolean>.startLoading() { this.value = true }
    private fun MutableState<Boolean>.stopLoading() { this.value = false }
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