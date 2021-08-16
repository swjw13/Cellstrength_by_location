package com.example.getcellinfos.Pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.getcellinfos.Pager.BluetoothActivity.BluetoothFragment
import com.example.getcellinfos.Pager.FiveG.FiveGFragment
import com.example.getcellinfos.Pager.WifiClass.WifiFragment
import com.example.getcellinfos.Pager.gnssActivity.GnssActivity

class PagerAdapter(private val fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val wifiFragment = WifiFragment(fragmentActivity)
    private val gnssActivity = GnssActivity(fragmentActivity)
    private val bluetoothFragment = BluetoothFragment(fragmentActivity)
    private val NRFragment = FiveGFragment(fragmentActivity)

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> wifiFragment
            1 -> gnssActivity
            2 -> bluetoothFragment
            else -> NRFragment
        }
    }

    fun getWifiStrength(): Int {
        return wifiFragment.powerful_strength
    }

    fun getGpsSatelliteStrength(): MutableList<Float> {
        return gnssActivity.gpsSateliteList
    }

    fun getNrStregnth(): List<Int> {
        return listOf(
            NRFragment.ssRsrp,
            NRFragment.ssRsrq,
            NRFragment.ssSinr
        )
    }
}