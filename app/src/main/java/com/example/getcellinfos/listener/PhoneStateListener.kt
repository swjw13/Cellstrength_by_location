package com.example.getcellinfos.listener

import android.os.Build
import android.telephony.*
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.getcellinfos.R


class phoneStateListener(val context: View) : PhoneStateListener() {

    var list = mutableListOf(0, 0, 0, 0, 0, 0)

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onSignalStrengthsChanged(signalStrength: SignalStrength?) {
        super.onSignalStrengthsChanged(signalStrength)

        signalStrength?.cellSignalStrengths?.forEach {
            if(it is CellSignalStrengthLte){
                list[0] = it.rsrp
                list[1] = it.rsrq
                list[2] = it.rssi
                list[3] = it.rssnr

                context.findViewById<TextView>(R.id.rsrpTextView).text = list[0].toString()
                context.findViewById<TextView>(R.id.rsrqTextView).text = list[1].toString()
                context.findViewById<TextView>(R.id.rssiTextView).text = list[2].toString()
                context.findViewById<TextView>(R.id.rssnrTextView).text = list[3].toString()
            }
        }
    }

    override fun onCellInfoChanged(cellInfo: MutableList<CellInfo>?) {
        super.onCellInfoChanged(cellInfo)

        if (cellInfo != null) {
            for (m in cellInfo) {
                if ((m is CellInfoLte) && m.isRegistered) {
                    list[4] = m.cellIdentity.earfcn
                    context.findViewById<TextView>(R.id.earfcnTextView).text = m.cellIdentity.earfcn.toString()

                    list[5] = m.cellIdentity.pci
                    context.findViewById<TextView>(R.id.pciTextView).text = m.cellIdentity.pci.toString()
                }
            }
        }
    }
}