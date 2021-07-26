package com.example.getcellinfos.threadActivity

import android.net.wifi.WifiManager
import android.widget.TextView
import com.example.getcellinfos.GlobalVariable
import java.util.*

class timerTask(
//    val view: TextView
    val wifiManager: WifiManager?
) : TimerTask() {
    override fun run() {
//        val global = view.context.applicationContext as GlobalVariable
//
//        view.text = global.phoneLatitude + " / " + global.phoneLongitude
//        wifiManager?.startScan()
    }
}