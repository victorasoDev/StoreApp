package uwu.victoraso.storeapp.ui.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
        if (accountService.hasUser()) openAndPopUp(MainDestinations.HOME_ROUTE, MainDestinations.SPLASH_ROUTE)
        else openAndPopUp(MainDestinations.LOGIN_ROUTE, MainDestinations.SPLASH_ROUTE)
    }
}