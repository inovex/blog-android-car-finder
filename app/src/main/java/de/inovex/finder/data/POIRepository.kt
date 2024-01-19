package de.inovex.finder.data

import de.inovex.finder.domain.dto.Properties
import de.inovex.finder.domain.model.POI
import de.inovex.finder.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class POIRepository(private val api: KomootApi) {

    suspend fun getNearBy(
        query: String, lat: Double, lon: Double
    ): Flow<Resource<List<POI>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.getNearBy(query, lat, lon)
                val pois = response.features.map {
                    POI(
                        name = it.properties.name ?: "Unknown",
                        address = it.properties.toAddress(),
                        lat = it.geometry.coordinates[0],
                        long = it.geometry.coordinates[1]
                    )
                }
                emit(Resource.Success(pois))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    private fun Properties.toAddress() =
        listOfNotNull(street, postcode, city, countryCode).joinToString(", ")
}