package uwu.victoraso.storeapp.ui.log.login

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uwu.victoraso.storeapp.MainDestinations
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.SnackbarManager
import uwu.victoraso.storeapp.model.service.AccountService
import uwu.victoraso.storeapp.model.service.StorageService
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import uwu.victoraso.storeapp.ui.utils.isValidEmail
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val accountService: AccountService,
    private val storageService: StorageService
): ViewModel() {

    var uiState = mutableStateOf(LoginUiState()) //TODO: cambiar por flows
        private set

    private val email get() = uiState.value.email
    private val password get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (password.isBlank()) {
            SnackbarManager.showMessage(R.string.empty_password_error)
            return
        }

        viewModelScope.launch {
            val oldUserId = accountService.getUserId()
            accountService.authenticate(email, password) { error ->
                if (error == null) {
                    linkWithEmail()
                    updateUserId(oldUserId, openAndPopUp)
                } else Log.d(DEBUG_TAG, error.toString())
            }
        }
    }

    private fun linkWithEmail() {
        viewModelScope.launch {
            accountService.linkAccount(email, password) { error ->
                if (error != null) Log.d(DEBUG_TAG, error.toString())
            }
        }
    }

    private fun updateUserId(oldUserId: String, openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch {
            val newUserId = accountService.getUserId()
            storageService.updateUserId(oldUserId, newUserId) { error ->
                if (error != null) Log.d(DEBUG_TAG, error.toString())
                else openAndPopUp(MainDestinations.HOME_ROUTE, MainDestinations.LOGIN_ROUTE)
            }
        }
    }

    fun onForgotPasswordClick() {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        viewModelScope.launch {
            accountService.sendRecoveryEmail(email) { error ->
                if (error != null) Log.d(DEBUG_TAG, error.toString())
                else SnackbarManager.showMessage(R.string.recovery_email_sent)
            }
        }
    }

}

data class LoginUiState(
    val email: String = "",
    val password: String = ""
)