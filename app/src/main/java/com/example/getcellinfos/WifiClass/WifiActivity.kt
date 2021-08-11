package com.example.getcellinfos.WifiClass

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.getcellinfos.R
import com.example.getcellinfos.WifiClass.WifiListener
import com.example.getcellinfos.WifiClass.WifiScanListener
import com.example.getcellinfos.WifiClass.WifiScanReceiver

class WifiActivity : AppCompatActivity() {

    private val wifiRecyclerView: RecyclerView by lazy {
        findViewById(R.id.wifiListRecyclerView)
    }

    private lateinit var adapter: WifiRecyclerViewAdapter

    private var wifiManager: WifiManager? = null
    private lateinit var wifiListener: WifiListener
    private lateinit var wifiScanReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi)

        initForActivity()
    }
    private fun initForActivity(){
        initWifiManager()
        startCheckingWifi()
        initRecyclerView()
    }

    private fun initRecyclerView(){
        adapter = WifiRecyclerViewAdapter()
        wifiRecyclerView.layoutManager = LinearLayoutManager(this)
        wifiRecyclerView.adapter = adapter
    }

    private fun initWifiManager() {
        wifiManager = getSystemService(WIFI_SERVICE) as WifiManager
        wifiListener = WifiScanListener { data ->
            updateManager(data)
        }
        wifiScanReceiver = WifiScanReceiver(wifiListener, wifiManager!!)
    }

    private fun startCheckingWifi() {
        setupWifiChecking()
        wifiManager?.startScan()
    }

    private fun setupWifiChecking() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        this.registerReceiver(wifiScanReceiver, intentFilter)
    }

    private fun updateManager(data: MutableList<WifiDataUnit>){
        adapter.list = data
        adapter.notifyDataSetChanged()
    }



    override fun onStop() {
        super.onStop()
        this.unregisterReceiver(wifiScanReceiver)
    }


}