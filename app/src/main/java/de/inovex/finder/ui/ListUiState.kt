package de.inovex.finder.ui

import de.inovex.finder.domain.model.POI

data class ListUiState(
    val pois: List<POI> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)