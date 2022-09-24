package uwu.victoraso.storeapp.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import uwu.victoraso.storeapp.ui.components.StoreAppDivider
import uwu.victoraso.storeapp.ui.theme.AlphaNearOpaque
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

@Composable
fun DestinationBar(
    modifier: Modifier = Modifier,
    title: String = "Welcome to StoreApp",
    onDestinationBarButtonClick: () -> Unit
) {
    Column(modifier = modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = StoreAppTheme.colors.uiBackground.copy(alpha = AlphaNearOpaque),
            contentColor = StoreAppTheme.colors.textSecondary,
            elevation = 0.dp
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1,
                color = StoreAppTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(start = 45.dp) //TODO habrá otra forma de centrarlo?
            )
            IconButton(
                onClick = { onDestinationBarButtonClick() },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ExpandMore,
                    tint = StoreAppTheme.colors.brand,
                    contentDescription = null
                )
            }
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
        DestinationBar(onDestinationBarButtonClick = { })
    }
}