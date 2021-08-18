package com.example.getcellinfos.Pager.fragments.WifiClass

import android.net.wifi.ScanResult
import android.util.Log

class WifiScanListener(val action: (MutableList<WifiDataUnit>) -> Unit) : WifiListener {
    override fun scanSuccess(list: List<ScanResult>) {
        list.sortedBy {
            it.level
        }
        val dataList = mutableListOf<WifiDataUnit>()
        for(i in list){
            if(i.SSID.isNotEmpty()) {
                dataList.add(WifiDataUnit(i.SSID, i.BSSID, i.level, i.capabilities, i.channelWidth, i.frequency))
            }
        }
        action(dataList)
    }

    override fun scanFailure() {
        Log.d("jae", "scan Failed")
    }
}