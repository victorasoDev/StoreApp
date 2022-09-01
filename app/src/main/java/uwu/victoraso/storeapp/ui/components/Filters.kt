package uwu.victoraso.storeapp.ui.components

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import uwu.victoraso.storeapp.model.Filter
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG

@Composable
fun FilterBar(
    filters: List<Filter>,
    onShowFilter: () -> Unit,
    /** TODO: Se podrá borrar en un futuro **/
    addRemoveCategory: ((Boolean, String) -> Unit)? = null,
//    removeCategory: ((Boolean) -> Unit)? = null,
) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 8.dp),
        modifier = Modifier.heightIn(min = 56.dp)
    ) {
//        item {
//            IconButton(onClick = onShowFilter) {
//                Icon(
//                    imageVector = Icons.Rounded.FilterList,
//                    tint = StoreAppTheme.colors.brand,
//                    contentDescription = "null",
//                    modifier = Modifier.diagonalGradientBorder(
//                        colors = StoreAppTheme.colors.interactiveSecondary,
//                        shape = CircleShape
//                    )
//                )
//            }
//        }
        items(filters) { filter ->
            FilterChip(filter = filter, shape = MaterialTheme.shapes.small, addRemoveCategory = addRemoveCategory)
        }
    }
}

@Composable
fun FilterChip(
    filter: Filter,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    addRemoveCategory: ((Boolean, String) -> Unit)? = null,
) {
    val (selected, setSelected) = filter.enabled //TODO: what is this
    val backgroundColor by animateColorAsState(
        if (selected) StoreAppTheme.colors.brandSecondary else StoreAppTheme.colors.uiBackground
    )
    val border = Modifier.fadeInDiagonalGradientBorder(
        showBorder = !selected,
        colors = StoreAppTheme.colors.interactiveSecondary,
        shape = shape
    )
    val textColor by animateColorAsState(
        if (selected) Color.Black else StoreAppTheme.colors.textSecondary
    )

    if (selected) {
        if (addRemoveCategory != null) {
            addRemoveCategory(true, filter.name)
        }
    } else {
        if (addRemoveCategory != null) {
            addRemoveCategory(false, filter.name)
        }
    }
    StoreAppSurface(
        modifier = Modifier.heightIn(28.dp),
        color = backgroundColor,
        contentColor = textColor,
        shape = shape,
        elevation = 2.dp
    ) {
        val interactionSource = remember { MutableInteractionSource() } //TODO: what is this

        val pressed by interactionSource.collectIsPressedAsState()
        val backgroundPressed =
            if (pressed) {
                Modifier.offsetGradientBackground(
                    StoreAppTheme.colors.interactiveSecondary,
                    200f,
                    0f
                )
            } else {
                Modifier.background(Color.Transparent)
            }
        Box(
            modifier = Modifier
                .toggleable(
                    value = selected,
                    onValueChange = setSelected,
                    interactionSource = interactionSource,
                    indication = null
                )
                .then(backgroundPressed)
                .then(border)
        ) {
            Text(
                text = filter.name,
                style = MaterialTheme.typography.caption,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )
        }
    }
}