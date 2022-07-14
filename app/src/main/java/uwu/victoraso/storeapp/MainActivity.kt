package uwu.victoraso.storeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import uwu.victoraso.storeapp.ui.components.StoreAppScaffold
import uwu.victoraso.storeapp.ui.home.HomeSections
import uwu.victoraso.storeapp.ui.home.StoreAppBottomBar
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StoreApp()
        }
    }
}