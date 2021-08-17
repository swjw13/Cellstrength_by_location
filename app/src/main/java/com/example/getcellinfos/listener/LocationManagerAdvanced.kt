package com.example.getcellinfos.listener

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class LocationManagerAdvanced(
    private val view: TextView?,
    private val changeMap: (Double, Double) -> Unit
) : LocationListener {

    private var latitude = 0.00
    private var longitude = 0.00
    private var altitude = 0.00

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
        altitude = location.altitude

        view?.text = "%.5f".format(latitude) + "/" + "%.5f".format(longitude) + "/" + "%.5f".format(altitude)
        changeMap(latitude, longitude)
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)

        Toast.makeText(view?.context, "Provider Enabled", Toast.LENGTH_SHORT).show()
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)

        Toast.makeText(view?.context, "Provider Disabled", Toast.LENGTH_SHORT).show()
    }

    fun getLocation(): List<Double>{
        return listOf(latitude,longitude,altitude)
    }
}