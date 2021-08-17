package com.example.getcellinfos.overallService

import android.app.Activity
import android.os.Build
import android.telephony.CellSignalStrengthLte
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.widget.TextView
import com.example.getcellinfos.R

class StrengthListener(val context: Activity): PhoneStateListener() {

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

                    context.findViewById<TextView>(R.id.rsrpTextView).text = "rsrp: ${strengthList[0]}"
                    context.findViewById<TextView>(R.id.rsrqTextView).text = "rsrq: ${strengthList[1]}"
                    context.findViewById<TextView>(R.id.rssiTextView).text = "rssi: ${strengthList[2]}"
                    context.findViewById<TextView>(R.id.rssnrTextView).text = "rssnr: ${strengthList[3]}"
                }
            }
        }
    }

    fun getStrengthList():MutableList<Int>{
        return strengthList
    }
}