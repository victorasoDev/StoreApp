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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

@Composable
fun StoreAppLoadingButton(
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    @StringRes defaultText: Int,
    @StringRes actionText: Int
) {
    StoreAppButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !isLoading,
    ) {
        AnimatedVisibility(visible = isLoading) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                        .padding(top = 4.dp, bottom = 4.dp, end = 8.dp),
                    strokeWidth = 2.dp
                )
            }
        }
        Text(text = if (isLoading) stringResource(actionText) else stringResource(defaultText))
    }
}

@Composable
fun StoreAppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = ButtonShape,
    border: BorderStroke? = null,
    backgroundGradient: List<Color> = StoreAppTheme.colors.interactiveSecondary,
    disabledBackgroundGradient: List<Color> = StoreAppTheme.colors.interactiveSecondary,
    contentColor: Color = StoreAppTheme.colors.textInteractive,
    color: Color = Color.Transparent,
    disabledContentColor: Color = StoreAppTheme.colors.textHelp,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    StoreAppSurface(
        shape = shape,
        color = color,
        contentColor = if (enabled) contentColor else disabledContentColor,
        border = border,
        modifier = modifier
            .clip(shape)
            .background(
                Brush.horizontalGradient(
                    colors = if (enabled) backgroundGradient else disabledBackgroundGradient
                )
            )
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null
            )
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.button) {
            Row(
                Modifier
                    .defaultMinSize(
                        minWidth = ButtonDefaults.MinWidth,
                        minHeight = ButtonDefaults.MinHeight
                    )
                    .indication(interactionSource, rememberRipple())
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

private val ButtonShape = RoundedCornerShape(percent = 50)

@Preview("default", "round")
@Preview("dark theme", "round", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", "round", fontScale = 2f)
@Composable
private fun ButtonPreview() {
    StoreAppTheme {
        StoreAppButton(onClick = {}) {
            Text(text = "Demo")
        }
    }
}

@Preview("default", "rectangle")
@Preview("dark theme", "rectangle", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", "rectangle", fontScale = 2f)
@Composable
private fun RectangleButtonPreview() {
    StoreAppTheme {
        StoreAppButton(
            onClick = {}, shape = RectangleShape
        ) {
            Text(text = "Demo")
        }
    }
}