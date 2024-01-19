package de.inovex.finder.domain.model

class POI(
    val name: String,
    val address: String = "",
    val long: Double,
    val lat: Double,
    val location: String = "$long, $lat"
)