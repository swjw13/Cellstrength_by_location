package com.example.getcellinfos

import android.app.Activity
import android.net.wifi.ScanResult
import android.widget.TextView

class WifiScanListener(val context: Activity): WifiListener {
    override fun scanSuccess(list: List<ScanResult>) {
        var text = ""
        list.forEach {
            text += it.SSID + " / " + it.level + " / " + it.timestamp + '\n'
        }
        context.findViewById<TextView>(R.id.cellWifiInfoTextView).text = text
    }

    override fun scanFailure() {
        context.findViewById<TextView>(R.id.cellWifiInfoTextView).text = "Scan Failure"
    }
}