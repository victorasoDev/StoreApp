package uwu.victoraso.storeapp.ui.components

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

@Composable
fun StoreAppToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable () -> Unit,
    checkedIcon: @Composable () -> Unit = icon,
    size: Dp = NiaToggleButtonDefaults.ToggleButtonSize,
    iconSize: Dp = NiaToggleButtonDefaults.ToggleButtonIconSize,
    backgroundColor: Color = Color.Transparent,
    checkedBackgroundColor: Color = StoreAppTheme.colors.uiBackground,
    iconColor: Color = contentColorFor(backgroundColor),
    checkedIconColor: Color = contentColorFor(checkedBackgroundColor)
) {
    val radius = with(LocalDensity.current) { (size / 2).toPx() }
    IconButton(
        onClick = { onCheckedChange(!checked) },
        modifier = modifier
            .size(size)
            .toggleable(value = checked, enabled = enabled, role = Role.Button, onValueChange = {})
            .drawBehind {
                drawCircle(
                    color = if (checked) checkedBackgroundColor else backgroundColor,
                    radius = radius
                )
            },
        enabled = enabled,
        content = {
            Box(
                modifier = Modifier.sizeIn(
                    maxWidth = iconSize,
                    maxHeight = iconSize
                )
            ) {
                val contentColor = if (checked) checkedIconColor else iconColor
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    if (checked) checkedIcon() else icon()
                }
            }
        }
    )
}

/**
 * Now in Android toggle button default values.
 */
object NiaToggleButtonDefaults {
    val ToggleButtonSize = 40.dp
    val ToggleButtonIconSize = 18.dp
}

@Preview("default", "rectangle")
@Preview("dark theme", "rectangle", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CheckedToggleButtonPreview() {
    StoreAppTheme {
        StoreAppToggleButton(
            checked = true,
            onCheckedChange = { },
            icon = { Icon(imageVector = Icons.Default.Favorite, contentDescription = "null") }
        )
    }
}

@Preview("default", "rectangle")
@Preview("dark theme", "rectangle", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UncheckedToggleButtonPreview() {
    StoreAppTheme {
        StoreAppToggleButton(
            checked = false,
            onCheckedChange = { },
            icon = { Icon(imageVector = Icons.Default.Favorite, contentDescription = "null") }
        )
    }
}