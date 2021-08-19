package com.example.getcellinfos.Pager.fragments.WifiClass

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context.WIFI_SERVICE
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    var green = 0
    var yellow = 0
    var orange = 0
    var red = 0

    var powerful_strength = 0

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

        powerful_strength = data[0].strength

        checkColor(data)
    }

    private fun checkColor(data: MutableList<WifiDataUnit>){
        green = 0
        yellow = 0
        orange = 0
        red = 0

        for (i in data) {
            when (i.strength) {
                in -70..0 -> green += 1
                in -85..-70 -> yellow += 1
                in -100..-85 -> orange += 1
                else -> red += 1
            }
        }
        view?.findViewById<TextView>(R.id.green_textView)?.text = green.toString()
        view?.findViewById<TextView>(R.id.yellow_textView)?.text = yellow.toString()
        view?.findViewById<TextView>(R.id.orange_textview)?.text = orange.toString()
        view?.findViewById<TextView>(R.id.red_textView)?.text = red.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(wifiScanReceiver)
    }
}