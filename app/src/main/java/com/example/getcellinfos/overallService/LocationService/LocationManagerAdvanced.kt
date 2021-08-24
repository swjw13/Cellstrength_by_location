package com.example.getcellinfos.overallService.LocationService

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener

class LocationManagerAdvanced(
    private val update: (Double, Double, Double) -> Unit,
    private val changeMap: (Double, Double) -> Unit
) : LocationListener {

    private var latitude = 0.00
    private var longitude = 0.00
    private var altitude = 0.00

    private var timestamp_tmp = 0L
    private var timeStamp = 0L

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
        altitude = location.altitude

        timeStamp = location.elapsedRealtimeNanos
        if (timeStamp - timestamp_tmp > 1000) {
            "실내"
        } else {
            "실외"
        }

        update(latitude, longitude, altitude)
        changeMap(latitude, longitude)
    }

    fun getLocation(): List<Double> {
        return listOf(latitude, longitude, altitude)
    }
}