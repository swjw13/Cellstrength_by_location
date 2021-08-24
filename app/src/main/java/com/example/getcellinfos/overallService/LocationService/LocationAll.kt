package com.example.getcellinfos.overallService.LocationService

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager

@SuppressLint("MissingPermission")
class LocationAll(context: Context){
    private val locationManager =
        context.getSystemService(Activity.LOCATION_SERVICE) as LocationManager
    private var mListener: LocationListener? = null

    // 리스너는 한번에 하나씩 달 수 있다
    // 다른 리스너를 달고 싶으면 리스너 제거 후 다시 달아주어야 한다.
    fun listenForLocationUpdate(type: String, listener: LocationListener) {
        if (mListener == null) {
            mListener = listener
        }
        locationManager.requestLocationUpdates(type, 0, 0.0F, mListener!!)
    }

    fun stopLocationUpdate() {
        if (mListener != null) {
            locationManager.removeUpdates(mListener!!)
            mListener = null
        }
    }

    fun getLocation(): List<Double>{
        return (mListener as LocationManagerAdvanced).getLocation()
    }

    fun isListenerAvailable(): Boolean{
        return mListener != null
    }
}