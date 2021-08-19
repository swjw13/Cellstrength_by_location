package com.example.getcellinfos.Pager.PagerMainActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.getcellinfos.Pager.fragments.SensorActivity.SensorFragment
import com.example.getcellinfos.Pager.fragments.FiveG.FiveGFragment
import com.example.getcellinfos.Pager.fragments.WifiClass.WifiFragment
import com.example.getcellinfos.Pager.Fragments.gnssActivity.GnssActivity

class PagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val wifiFragment = WifiFragment(fragmentActivity)
    private val gnssActivity = GnssActivity(fragmentActivity)
    private val sensorFragment = SensorFragment(fragmentActivity)
    private val NRFragment = FiveGFragment(fragmentActivity)

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> wifiFragment
            1 -> gnssActivity
            2 -> sensorFragment
            else -> NRFragment
        }
    }

    fun getWifiStrength(): Int {
        return wifiFragment.powerful_strength
    }

    fun getGpsSatelliteStrength(): MutableList<Float> {
        return gnssActivity.getCallback().gpsSateliteList
    }

    fun getNrStregnth(): List<Int> {
        return listOf(
            NRFragment.ssRsrp,
            NRFragment.ssRsrq,
            NRFragment.ssSinr
        )
    }
    fun getLightPower(): Float{
        return sensorFragment.light_strength
    }
}