package uwu.victoraso.storeapp.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uwu.victoraso.storeapp.ui.components.StoreAppDivider
import uwu.victoraso.storeapp.ui.theme.AlphaNearOpaque
import uwu.victoraso.storeapp.ui.theme.Neutral8
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.mirroringBackIcon

@Composable
fun StoreAppTopBar(
    modifier: Modifier = Modifier,
    screenTitle: String,
    upPress: () -> Unit
) {
    Column(modifier = modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = StoreAppTheme.colors.brand.copy(alpha = AlphaNearOpaque),
            contentColor = StoreAppTheme.colors.textSecondary,
            elevation = 0.dp,
        ) {
            IconButton(
                onClick = upPress,
                modifier = Modifier
                    .statusBarsPadding()
                    .weight(0.2f)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .size(36.dp)
                    .background(
                        color = Neutral8.copy(alpha = 0.32f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = mirroringBackIcon(),
                    tint = StoreAppTheme.colors.iconInteractive,
                    contentDescription = null
                )
            }
            Text(
                text = screenTitle,
                style = MaterialTheme.typography.subtitle1,
                fontSize = 18.sp,
                color = StoreAppTheme.colors.textInteractive,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 60.dp)
                    .weight(1f)
            )
        }
        StoreAppDivider()
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun PreviewDestinationBar() {
    StoreAppTheme {
        StoreAppTopBar(upPress = { }, screenTitle = "<Untitled>")
    }
}