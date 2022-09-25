package uwu.victoraso.storeapp.ui.log.signup

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.ktx.performance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uwu.victoraso.storeapp.MainDestinations
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.SnackbarManager
import uwu.victoraso.storeapp.model.service.AccountService
import uwu.victoraso.storeapp.model.service.StorageService
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import uwu.victoraso.storeapp.ui.utils.isValidEmail
import uwu.victoraso.storeapp.ui.utils.passwordMatches
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject
constructor(
    private val accountService: AccountService,
    private val storageService: StorageService
): ViewModel() {

    var uiState = mutableStateOf(SignUpUiState()) //TODO: cambiar por flows
        private set

    private val email get() = uiState.value.email
    private val password get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (password.isBlank()) {
            SnackbarManager.showMessage(R.string.empty_password_error)
            return
        }

        if (!password.passwordMatches(uiState.value.repeatPassword)) {
            SnackbarManager.showMessage(R.string.password_match_error)
            return
        }

        viewModelScope.launch {
            val oldUserId = accountService.getUserId()
            val createAccountTrace = Firebase.performance.newTrace(CREATE_ACCOUNT_TRACE)
            createAccountTrace.start()

            accountService.createAccount(email, password) { error ->
                createAccountTrace.stop()

                if (error == null) {
                    linkWithEmail()
                    updateUserId(oldUserId, openAndPopUp)
                } else Log.d(DEBUG_TAG, "$error")
            }
        }
    }

    private fun linkWithEmail() {
        viewModelScope.launch {
            accountService.linkAccount(email, password) { error ->
                if (error != null) { Log.d(DEBUG_TAG, "NonFatalCrash") } //TODO
            }
        }
    }

    private fun updateUserId(oldUserId: String, openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch {
            val newUserId = accountService.getUserId()

            storageService.updateUserId(oldUserId, newUserId) { error ->
                if (error != null) { Log.d(DEBUG_TAG, "NonFatalCrash") } //TODO
                else openAndPopUp(MainDestinations.HOME_ROUTE, MainDestinations.SIGNUP_ROUTE)
            }
        }
    }

    companion object {
        private const val CREATE_ACCOUNT_TRACE = "createAccount"
    }
}

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)