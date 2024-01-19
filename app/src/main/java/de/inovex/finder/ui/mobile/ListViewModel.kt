package de.inovex.finder.ui.mobile

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.inovex.finder.data.POIRepository
import de.inovex.finder.ui.ListUiState
import de.inovex.finder.util.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ListViewModel(
    private val repository: POIRepository
) : ViewModel() {
    var state by mutableStateOf(ListUiState())
    var searchQuery = mutableStateOf("ice cream")

    fun onSearchQueryChange(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun getPOIs(location: Location) {
        viewModelScope.launch {
            repository.getNearBy(searchQuery.value, location.latitude, location.longitude)
                .onEach {
                    state = when (it) {
                        is Resource.Success -> {
                            ListUiState(
                                pois = it.data,
                            )
                        }

                        is Resource.Error -> {
                            ListUiState(
                                error = it.errorMessage,
                            )
                        }

                        is Resource.Loading -> {
                            ListUiState(
                                pois = state.pois,
                                loading = true,
                            )
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }
}