package com.example.getcellinfos

import android.app.Application
import kotlin.properties.Delegates

open class GlobalVariable: Application() {

    lateinit var phoneLatitude: String
    lateinit var phoneLongitude: String
    lateinit var phoneAltitude: String
    var locationTimeInMillis by Delegates.notNull<Long>()

    lateinit var phoneStrength: String
    var phoneStrength_millisecond by Delegates.notNull<Long>()
    lateinit var phoneLocation: String
    var phoneLocation_millisecond by Delegates.notNull<Long>()

    override fun onCreate() {
        super.onCreate()
        phoneLongitude = ""
        phoneLatitude = ""
    }

}