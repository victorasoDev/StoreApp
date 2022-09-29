package uwu.victoraso.storeapp.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uwu.victoraso.storeapp.MainDestinations
import uwu.victoraso.storeapp.model.service.AccountService
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject
constructor(
    private val accountService: AccountService,
) : ViewModel() {

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch {
            if (accountService.hasUser()) openAndPopUp(MainDestinations.HOME_ROUTE, MainDestinations.SPLASH_ROUTE)
            else openAndPopUp(MainDestinations.LOGIN_ROUTE, MainDestinations.SPLASH_ROUTE)
        }
    }
}