package uwu.victoraso.storeapp.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

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

@Composable
fun StoreAppDialogButton(
    onClick: () -> Unit,
    @StringRes stringID: Int,
    modifier: Modifier = Modifier
) {
    StoreAppButton(
        onClick = { onClick() },
        modifier = modifier.padding(16.dp),
    ) {
        Text(
            text = stringResource(id = stringID),
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@Composable
fun StoreAppDialogTitle(
    @StringRes stringID: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(colors = StoreAppTheme.colors.gradient2_2)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = stringID),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle1,
            color = Color.White
        )
    }
}