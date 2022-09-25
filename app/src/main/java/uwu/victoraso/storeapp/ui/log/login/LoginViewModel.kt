package uwu.victoraso.storeapp.ui.log.login

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uwu.victoraso.storeapp.R
import uwu.victoraso.storeapp.model.SnackbarManager
import uwu.victoraso.storeapp.ui.utils.isValidEmail
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(

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

        }
    }


}

data class LoginUiState(
    val email: String = "",
    val password: String = ""
)