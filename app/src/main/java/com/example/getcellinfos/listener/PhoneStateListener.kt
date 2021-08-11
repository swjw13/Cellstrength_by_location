package com.example.getcellinfos.listener

import android.os.Build
import android.telephony.*
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.getcellinfos.R


class phoneStateListener(val context: View) : PhoneStateListener() {

    var list = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

    override fun onSignalStrengthsChanged(signalStrength: SignalStrength?) {
        super.onSignalStrengthsChanged(signalStrength)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            signalStrength?.cellSignalStrengths?.forEach {
                if (it is CellSignalStrengthLte) {
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
    }

    override fun onCellInfoChanged(cellInfo: MutableList<CellInfo>?) {
        super.onCellInfoChanged(cellInfo)

        if (cellInfo != null) {

            list[6] = cellInfo.size
            context.findViewById<TextView>(R.id.neighborcellTextView).text = list[6].toString()

            for (m in cellInfo) {
                if ((m is CellInfoLte) && m.isRegistered) {
                    list[4] = m.cellIdentity.earfcn
                    context.findViewById<TextView>(R.id.earfcnTextView).text =
                        list[4].toString()

                    list[5] = m.cellIdentity.pci
                    context.findViewById<TextView>(R.id.pciTextView).text =
                        list[5].toString()

                    val bit_enbId = 0b1111111111111111111100000000
                    val bit_cellnum = 0b11111100
                    list[7] = (m.cellIdentity.ci and bit_enbId) shr 8
                    list[8] = m.cellIdentity.ci and bit_cellnum shr 2
                }
            }
        }
    }
}