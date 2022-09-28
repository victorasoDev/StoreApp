package uwu.victoraso.storeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            StoreApp()
        }
    }
}

//TODO: notifications -> https://developer.android.com/training/notify-user/build-notification
//TODO: sendEmail (cuando hagas el checkOut) -> https://www.geeksforgeeks.org/send-email-in-an-android-application-using-jetpack-compose/
//TODO: login -> https://firebase.blog/posts/2022/05/adding-firebase-auth-to-jetpack-compose-app && https://firebase.google.com/docs/auth/android/firebaseui && https://stackoverflow.com/a/67156998/19807589