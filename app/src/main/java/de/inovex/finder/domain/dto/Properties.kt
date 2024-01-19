package de.inovex.finder.domain.dto

import com.google.gson.annotations.SerializedName

data class Properties(
    val name: String?,
    val postcode: String,
    val city: String?,
    val street: String?,
    val state: String?,
    val country: String,
    @SerializedName("countrycode")
    val countryCode: String,
    @SerializedName("osm_id")
    val osmId: Long,
    @SerializedName("osm_value")
    val osmValue: String,
)