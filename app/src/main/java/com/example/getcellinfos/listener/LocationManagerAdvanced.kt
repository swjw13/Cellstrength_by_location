package com.example.getcellinfos.listener

import android.location.Location
import android.location.LocationListener
import android.widget.TextView
import android.widget.Toast
import com.example.getcellinfos.GlobalVariable

class LocationManagerAdvanced(val view: TextView?) : LocationListener {

    override fun onLocationChanged(location: Location) {

        val latitude = location.latitude
        val longitude = location.longitude
        val altitude = location.altitude
//        changeViewFromState(latitude.toString() + " / " + longitude.toString())

        val global = view?.context?.applicationContext as GlobalVariable
        global.phoneLatitude = location.latitude.toString()
        global.phoneLongitude = location.longitude.toString()
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)

        Toast.makeText(view?.context, "Provider Enabled" + provider, Toast.LENGTH_SHORT).show()
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
        view?.text = "Provider Disabled"
        Toast.makeText(view?.context, "Provider Disabled" + provider, Toast.LENGTH_SHORT).show()
    }

    private fun changeViewFromState(text: String) {
//        context.findViewById<TextView>(R.id.cellGPSlocationInfoTextview).text = text
        view?.text = text
    }
}