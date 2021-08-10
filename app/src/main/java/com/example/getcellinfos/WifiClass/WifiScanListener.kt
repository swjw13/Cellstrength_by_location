package com.example.getcellinfos.WifiClass

import android.app.Activity
import android.net.wifi.ScanResult
import android.util.Log

class WifiScanListener(val context: Activity) : WifiListener {
    override fun scanSuccess(list: List<ScanResult>) {
        list.sortedBy {
            it.level
        }
        Log.d("jae", list.toString())
    }

    override fun scanFailure() {
//        context.findViewById<TextView>(R.id.cellWifiInfoTextView).text = "Scan Failure"
    }
}