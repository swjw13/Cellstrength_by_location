package com.example.getcellinfos.WifiClass

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context.WIFI_SERVICE
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.getcellinfos.R

class WifiFragment(val fragmentActivity: FragmentActivity) : Fragment() {

    private lateinit var adapter: WifiRecyclerViewAdapter
    private var wifiRecyclerView: RecyclerView? = null

    private var wifiManager: WifiManager? = null
    private lateinit var wifiListener: WifiListener
    private lateinit var wifiScanReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_wifi, container, false)
    }

    override fun onStart() {
        super.onStart()
        initForActivity()
    }

    private fun initForActivity() {
        initRecyclerView()
        initWifiManager()
    }

    override fun onResume() {
        super.onResume()
        startCheckingWifi()
    }

    private fun initRecyclerView() {
        wifiRecyclerView = view?.findViewById(R.id.wifiListRecyclerView)
        adapter = WifiRecyclerViewAdapter()
        wifiRecyclerView?.layoutManager = LinearLayoutManager(fragmentActivity)
        wifiRecyclerView?.adapter = adapter
    }

    private fun initWifiManager() {
        wifiManager = context?.getSystemService(WIFI_SERVICE) as WifiManager
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
        context?.registerReceiver(wifiScanReceiver, intentFilter)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateManager(data: MutableList<WifiDataUnit>) {
        adapter.list = data
        adapter.notifyDataSetChanged()
    }


    override fun onStop() {
        super.onStop()
        context?.unregisterReceiver(wifiScanReceiver)
    }
}