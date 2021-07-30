package com.example.getcellinfos.GlobalApplication

import android.app.Application
import kotlin.properties.Delegates

open class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    companion object{
        var phoneLatitude: Double = 0.0
        var phoneLongitude: Double = 0.0
        var phoneAltitude: Double = 0.0
        var locationTimestamp: Long = 0L

        var phoneRSSI: Int = 0
        var phoneRSRP: Int = 0
        var phoneRSRQ: Int = 0

        var WifiList: String = ""
    }
}