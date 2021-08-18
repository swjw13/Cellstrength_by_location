package com.example.getcellinfos.Pager.fragments.WifiClass

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager

class WifiScanReceiver(private val listener: WifiListener, private val wifiManager: WifiManager) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val success = intent?.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
        if (success == true) {
            listener.scanSuccess(wifiManager.scanResults)
        } else {
            listener.scanFailure()
        }
    }
}