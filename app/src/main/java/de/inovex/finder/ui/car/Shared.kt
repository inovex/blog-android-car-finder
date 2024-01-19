package de.inovex.finder.ui.car

import androidx.car.app.model.CarColor
import androidx.car.app.model.CarLocation
import androidx.car.app.model.MessageTemplate
import androidx.car.app.model.Metadata
import androidx.car.app.model.Place
import androidx.car.app.model.PlaceMarker
import androidx.car.app.model.Row
import de.inovex.finder.domain.model.POI

fun buildPOIItem(poi: POI, clickListener: (poi: POI) -> Unit): Row {
    val place = Place.Builder(
        CarLocation.create(
            poi.long, poi.lat
        )
    ).setMarker(
        PlaceMarker.Builder()
            .setColor(CarColor.createCustom(0xFF543a20.toInt(), 0xFF543a20.toInt())).build()
    ).build()

    val metadata = Metadata.Builder().setPlace(place).build()

    return Row.Builder()
        .setTitle(poi.name)
        .setMetadata(metadata)
        .setBrowsable(true)
        .setOnClickListener {
            clickListener(poi)
        }
        .addText(poi.address)
        .build()
}

fun buildErrorMessageTemplate(text: String) =
    MessageTemplate.Builder(text).setTitle("Error").build()