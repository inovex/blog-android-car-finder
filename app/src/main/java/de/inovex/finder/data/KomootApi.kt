package de.inovex.finder.data

import de.inovex.finder.domain.dto.KomootDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface KomootApi {
    @GET("api")
    suspend fun getNearBy(
        @Query("q") query: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): KomootDTO
}