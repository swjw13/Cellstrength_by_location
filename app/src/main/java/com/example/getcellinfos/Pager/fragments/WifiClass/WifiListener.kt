package com.example.getcellinfos.Pager.fragments.WifiClass

import android.net.wifi.ScanResult

interface WifiListener {
    fun scanSuccess(list: List<ScanResult>)
    fun scanFailure()
}