package uwu.victoraso.storeapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun StoreAppDialog(
    onDismiss: () -> Unit,
    properties: DialogProperties,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = properties
    ) {
        StoreAppCard(
            modifier = modifier
                .fillMaxWidth()
        ) {
            CompositionLocalProvider(content = content)
        }
    }
}