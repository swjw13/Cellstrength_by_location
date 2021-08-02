package com.example.getcellinfos.WifiClass

import android.app.Activity
import android.net.wifi.ScanResult
import android.widget.TextView
import com.example.getcellinfos.GlobalApplication.GlobalApplication
import com.example.getcellinfos.R

class WifiScanListener(val context: Activity) : WifiListener {
    override fun scanSuccess(list: List<ScanResult>) {
        var text = "wifi list \n"
        list.forEach {
            text += it.SSID + " / " + it.level + " / " + it.timestamp + '\n'
        }
//        context.findViewById<TextView>(R.id.cellWifiInfoTextView).text = text

        GlobalApplication.WifiList = text
    }

    override fun scanFailure() {
//        context.findViewById<TextView>(R.id.cellWifiInfoTextView).text = "Scan Failure"
    }
}