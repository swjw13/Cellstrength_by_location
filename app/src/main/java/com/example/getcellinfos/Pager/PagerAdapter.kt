package com.example.getcellinfos.Pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.getcellinfos.Pager.BluetoothActivity.BluetoothFragment
import com.example.getcellinfos.Pager.FiveG.FiveGFragment
import com.example.getcellinfos.Pager.WifiClass.WifiFragment
import com.example.getcellinfos.Pager.gnssActivity.GnssActivity

class PagerAdapter(private val fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> WifiFragment(fragmentActivity)
            1 -> GnssActivity(fragmentActivity)
            2 -> BluetoothFragment(fragmentActivity)
            else -> FiveGFragment(fragmentActivity)
        }
    }
}