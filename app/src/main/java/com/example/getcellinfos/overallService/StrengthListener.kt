package com.example.getcellinfos.overallService

import android.app.Activity
import android.os.Build
import android.telephony.CellSignalStrengthLte
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.widget.TextView
import com.example.getcellinfos.R

class StrengthListener(val update: (List<Int>) -> Unit): PhoneStateListener() {

    private var strengthList = mutableListOf(0,0,0,0)

    override fun onSignalStrengthsChanged(signalStrength: SignalStrength?) {
        super.onSignalStrengthsChanged(signalStrength)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            signalStrength?.cellSignalStrengths?.forEach {
                if (it is CellSignalStrengthLte) {
                    strengthList[0] = it.rsrp
                    strengthList[1] = it.rsrq
                    strengthList[2] = it.rssi
                    strengthList[3] = it.rssnr

                    update(strengthList)
                }
            }
        }
    }

    fun getStrengthList():MutableList<Int>{
        return strengthList
    }
}