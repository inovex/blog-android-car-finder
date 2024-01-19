package de.inovex.finder.ui.mobile

import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.inovex.finder.domain.model.POI
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun POIListComposable(
    onItemClicked: (pois: POI) -> Unit,
    getLocation: (block: (Location?) -> Unit) -> Unit,
    allPermissionsGranted: Boolean
) {
    val viewModel = koinViewModel<ListViewModel>()
    val coroutineScope = rememberCoroutineScope()

    fun refresh() = coroutineScope.launch {
        getLocation {
            if (it != null) {
                viewModel.getPOIs(it)
            }
        }
    }

    LaunchedEffect(allPermissionsGranted) {
        refresh()
    }

    List(
        searchQuery = viewModel.searchQuery.value,
        onSearchQueryChange = {
            viewModel.onSearchQueryChange(it)
            refresh()
        }, onItemClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun List(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onItemClicked: (poi: POI) -> Unit,
) {
    val viewModel = koinViewModel<ListViewModel>()
    val state = viewModel.state

    SearchBar(
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        onSearch = {},
        placeholder = {
            Text(text = "Search POIs")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        },
        active = true,
        onActiveChange = {},
        content = {
            if (state.error != null) {
                Error(text = state.error)
            } else {
                if (state.pois.isEmpty()) {
                    Error(text = "No Results")
                } else {
                    LazyColumn {
                        items(state.pois, itemContent = { poi ->
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onItemClicked(poi)
                                    }) {
                                Card(poi)
                            }
                        })
                    }
                }
            }
        }
    )
}

@Composable
fun Card(poi: POI) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(all = 6.dp)
    ) {
        Column(Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
            Text(
                text = poi.name,
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
            )
            Text(
                text = poi.address,
                style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic),
                color = Color.DarkGray
            )
            Text(
                text = poi.location,
                style = TextStyle(fontSize = 14.sp, fontStyle = FontStyle.Italic),
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun Error(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        textAlign = TextAlign.Center,
        text = text
    )
}