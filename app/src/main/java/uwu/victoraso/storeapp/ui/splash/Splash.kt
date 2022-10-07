package uwu.victoraso.storeapp.ui.splash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import uwu.victoraso.storeapp.ui.components.StoreAppCircularIndicator
import uwu.victoraso.storeapp.ui.components.StoreAppSurface

private const val SPLASH_TIMEOUT = 1000L

@Composable
fun Splash(
    openAndPopUp: (String, String) -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    StoreAppSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        StoreAppCircularIndicator()
    }

    /** Launch in a Coroutine if StoreApp has an user logged. Only recomposes if key1 changes (this -> Never because hardcoded) **/
    LaunchedEffect(key1 = true) {
        delay(SPLASH_TIMEOUT)
        viewModel.onAppStart(openAndPopUp)
    }
}