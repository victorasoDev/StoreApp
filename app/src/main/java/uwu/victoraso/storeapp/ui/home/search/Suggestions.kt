package uwu.victoraso.storeapp.ui.home.search

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uwu.victoraso.storeapp.model.SearchRepo
import uwu.victoraso.storeapp.model.SearchSuggestionGroup
import uwu.victoraso.storeapp.ui.components.StoreAppSurface
import uwu.victoraso.storeapp.ui.theme.StoreAppTheme
import kotlin.math.min

@Composable
fun SearchSuggestions(
    suggestions: List<SearchSuggestionGroup>,
    onSuggestionSelect: (String) -> Unit
) {
    LazyColumn {
        suggestions.forEach { suggestionGroup ->
            item {
                SuggestionHeader(suggestionGroup.name)
            }
            items(suggestionGroup.suggestions) { suggestion ->
                Suggestion(
                    suggestion = suggestion,
                    onSuggestionSelect = onSuggestionSelect,
                    modifier = Modifier.fillParentMaxWidth()
                )
            }
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun SuggestionHeader(
    name: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = name,
    style = MaterialTheme.typography.h6,
    color = StoreAppTheme.colors.textPrimary,
    modifier = modifier
        .heightIn(min = 56.dp)
        .padding(horizontal = 24.dp, vertical = 4.dp)
        .wrapContentHeight()
    )
}

@Composable
private fun Suggestion(
    suggestion: String,
    onSuggestionSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = suggestion,
        style = MaterialTheme.typography.subtitle1,
        modifier = modifier
            .heightIn(min = 48.dp)
            .clickable { onSuggestionSelect(suggestion) }
            .padding(start = 24.dp)
            .wrapContentSize(Alignment.CenterStart)
    )
}


@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
fun PreviewSuggestions() {
    StoreAppTheme {
        StoreAppSurface {
            SearchSuggestions(
                suggestions = SearchRepo.getSuggestions(),
                onSuggestionSelect = { }
            )
        }
    }
}