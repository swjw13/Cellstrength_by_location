package com.example.getcellinfos

import android.app.Application

open class GlobalVariable: Application() {

    lateinit var phoneLatitude: String
    lateinit var phoneLongitude: String
    lateinit var phoneAltitude: String
    lateinit var phoneStrength: String
    lateinit var phoneStrength_millisecond:String
    lateinit var phoneLocation: String


    override fun onCreate() {
        super.onCreate()
        phoneLongitude = ""
        phoneLatitude = ""
    }

}