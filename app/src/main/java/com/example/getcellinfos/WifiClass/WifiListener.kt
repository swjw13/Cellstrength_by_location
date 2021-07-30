package com.example.getcellinfos.WifiClass

import android.net.wifi.ScanResult

interface WifiListener {
    fun scanSuccess(list: List<ScanResult>)
    fun scanFailure()
}