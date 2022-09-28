package uwu.victoraso.storeapp.ui.log.signup

import android.util.Log
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
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject
constructor(
    private val accountService: AccountService,
    private val storageService: StorageService
): ViewModel() {

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit, signUpUiFields: SignUpUiFields) {
        if (!signUpUiFields.email.isValidEmail()) {
            SnackbarManager.showMessage(R.string.email_error)
            return
        }

        if (signUpUiFields.password.isBlank()) {
            SnackbarManager.showMessage(R.string.empty_password_error)
            return
        }

        if (!signUpUiFields.password.passwordMatches(signUpUiFields.repeatPassword)) {
            SnackbarManager.showMessage(R.string.password_match_error)
            return
        }

        viewModelScope.launch {
            val oldUserId = accountService.getUserId()
            val createAccountTrace = Firebase.performance.newTrace(CREATE_ACCOUNT_TRACE)
            createAccountTrace.start()

            accountService.createAccount(signUpUiFields.email, signUpUiFields.password) { error ->
                createAccountTrace.stop()

                if (error == null) {
                    linkWithEmail(signUpUiFields)
                    updateUserId(oldUserId, openAndPopUp)
                } else Log.d(DEBUG_TAG, "$error")
            }
        }
    }

    private fun linkWithEmail(signUpUiFields: SignUpUiFields) {
        viewModelScope.launch {
            accountService.linkAccount(signUpUiFields.email, signUpUiFields.password) { error ->
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

data class SignUpUiFields(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)