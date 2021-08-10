package com.example.getcellinfos.activities

import android.content.BroadcastReceiver
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.getcellinfos.R
import com.example.getcellinfos.WifiClass.WifiListener
import com.example.getcellinfos.WifiClass.WifiScanListener
import com.example.getcellinfos.WifiClass.WifiScanReceiver

class WifiActivity : AppCompatActivity() {

    private var wifiManager: WifiManager? = null
    private lateinit var wifiListener: WifiListener
    private lateinit var wifiScanReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi)

        initWifiManager()
    }

        private fun initWifiManager() {
        wifiManager = getSystemService(WIFI_SERVICE) as WifiManager
        wifiListener = WifiScanListener(this)
        wifiScanReceiver = WifiScanReceiver(wifiListener, wifiManager!!)
    }


//    private fun startCheckingWifi() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            wifiManager?.registerScanResultsCallback(mainExecutor,
//                object : WifiManager.ScanResultsCallback() {
//                    override fun onScanResultsAvailable() {
//                        Log.d("테스트", "Wifi change detected@@@@@")
//                    }
//                })
//        } else {
//            setupWifiChecking()
//            wifiManager?.startScan()
//        }
//    }
//
//    private fun setupWifiChecking() {
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
//        applicationContext.registerReceiver(wifiScanReceiver, intentFilter)
//    }
}