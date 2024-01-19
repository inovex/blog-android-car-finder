package de.inovex.finder.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
fun getLocation(context: Context, block: (location: Location?) -> Unit) {
    LocationServices.getFusedLocationProviderClient(context).lastLocation
        .addOnSuccessListener { location: Location? ->
            block(location)
        }
}