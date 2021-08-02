package com.example.getcellinfos.GlobalApplication

import android.app.Application
import kotlin.properties.Delegates

open class GlobalApplication : Application() {

    companion object{
        var WifiList: String = ""
    }
}