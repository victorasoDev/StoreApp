package uwu.victoraso.storeapp.ui.home.cart

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
/**
 * Holds the Swipe to dismiss composable, its animation and the current state
 */
fun SwipeDismissItem(
    modifier: Modifier = Modifier,
    directions: Set<DismissDirection> = setOf(DismissDirection.EndToStart),
    enter: EnterTransition = expandVertically(),
    exit: ExitTransition = shrinkVertically(),
    background: @Composable (offset: Dp) -> Unit,
    content: @Composable (isDismissed: Boolean) -> Unit
) {
    //Hold the current state from the Swipe to Dismiss composable
    val dismissState = rememberDismissState()
    //Boolean value used for hiding the item if the current state is dismissed
    val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
    //Returns the swiped value in dp
    val offset = with(LocalDensity.current) { dismissState.offset.value.toDp() }

    AnimatedVisibility(
        visible = !isDismissed,
        modifier = modifier,
        enter = enter,
        exit = exit
    ) {
        SwipeToDismiss(
            modifier = modifier,
            state = dismissState,
            directions = directions,
            background = { background(offset) },
            dismissContent = { content(isDismissed) }
        )
    }
}

@Composable
fun SwipeDismissBehavior(
    content: @Composable () -> Unit
) {
    SwipeDismissItem(
        background = { offsetX ->
            /*Background color changes from light gray to red when the
            swipe to delete with exceeds 160.dp*/
            val backgroundColor = if (offsetX < -160.dp) {
                StoreAppTheme.colors.error
            } else {
                StoreAppTheme.colors.uiFloated
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(backgroundColor),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                // Set 4.dp padding only if offset is bigger than 160.dp
                val padding: Dp by animateDpAsState(
                    if (offsetX > -160.dp) 4.dp else 0.dp
                )
                Box(
                    Modifier
                        .width(offsetX * -1)
                        .padding(padding)
                ) {
                    // Height equals to width removing padding
                    val height = (offsetX + 8.dp) * -1
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height)
                            .align(Alignment.Center),
                        shape = CircleShape,
                        color = StoreAppTheme.colors.error
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            // Icon must be visible while in this width range
                            if (offsetX < (-40).dp && offsetX > (-152).dp) {
                                // Icon alpha decreases as it is about to disappear
                                val iconAlpha: Float by animateFloatAsState(
                                    if (offsetX < (-120).dp) 0.5f else 1f
                                )

                                Icon(
                                    imageVector = Icons.Filled.DeleteForever,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .graphicsLayer(alpha = iconAlpha),
                                    tint = StoreAppTheme.colors.uiBackground
                                )
                            }
                            /*Text opacity increases as the text is supposed to appear in
                            the screen*/
                            val textAlpha by animateFloatAsState(
                                if (offsetX > (-144).dp) 0.5f else 1f
                            )
                            if (offsetX < (-120).dp) {
                                Text(
                                    text = stringResource(id = uwu.victoraso.storeapp.R.string.remove_item),
                                    style = MaterialTheme.typography.subtitle1,
                                    color = StoreAppTheme.colors.uiBackground,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .graphicsLayer(
                                            alpha = textAlpha
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    ) {
        CompositionLocalProvider(content = content)
    }
}