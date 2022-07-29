package uwu.victoraso.storeapp.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

private const val DividerAlpha = 0.12f

@Composable
fun StoreAppDivider(
    modifier: Modifier = Modifier,
    color: Color = StoreAppTheme.colors.uiBorder.copy(alpha = DividerAlpha),
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp
) {
    Divider(
        modifier = modifier,
        color = color,
        thickness = thickness,
        startIndent = startIndent
    )
}

@Preview("default", showBackground = true)
@Preview("dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DividerPreview() {
    StoreAppTheme {
        Box(Modifier.size(height = 10.dp, width = 100.dp)) {
            StoreAppDivider(Modifier.align(Alignment.Center))
        }
    }
}