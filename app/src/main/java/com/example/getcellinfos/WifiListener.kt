package com.example.getcellinfos

import android.net.wifi.ScanResult

interface WifiListener {
    fun scanSuccess(list: List<ScanResult>)
    fun scanFailure()
}