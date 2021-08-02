package com.example.getcellinfos.listener

import android.app.Activity
import android.os.Build
import android.telephony.*
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.getcellinfos.GlobalApplication.GlobalApplication
import com.example.getcellinfos.R
import com.example.getcellinfos.dataClass.StrengthInfo
import kotlin.math.sign


class phoneStateListener(val context: View) : PhoneStateListener() {

    var strengths: MutableList<StrengthInfo> = mutableListOf()
    var list = listOf(0, 0, 0, 0, 0, 0)

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onSignalStrengthsChanged(signalStrength: SignalStrength?) {
        super.onSignalStrengthsChanged(signalStrength)

        val result = extractInfoFromStrength(signalStrength)

        if (result.isNotEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                strengths.add(
                    StrengthInfo(
                        result[0],  // rssi
                        result[1],  // rsrp
                        result[2],  // rsrq
                        signalStrength?.timestampMillis
                    )
                )
            } else {
                strengths.add(
                    StrengthInfo(
                        result[0],
                        result[1],
                        result[2],
                        System.currentTimeMillis()
                    )
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun extractInfoFromStrength(signalStrength: SignalStrength?): List<Int> {

        return if ((signalStrength != null) && (signalStrength.cellSignalStrengths.size > 0)) {
            val sStrength = signalStrength.cellSignalStrengths[0].toString()

            val list = sStrength.split(" ")

            val rssi = list[1].substring(5, list[1].length).toInt()
            val rsrp = signalStrength.cellSignalStrengths.get(0)?.dbm ?: -1
            val rsrq = list[3].substring(5, list[3].length).toInt()

            listOf(rssi, rsrp, rsrq)
        } else {
            listOf()
        }
    }


    override fun onCellInfoChanged(cellInfo: MutableList<CellInfo>?) {
        super.onCellInfoChanged(cellInfo)

        if (cellInfo != null) {
            for (m in cellInfo) if (m is CellInfoLte) {
                rsrp = m.cellSignalStrength.rsrp
                context.findViewById<TextView>(R.id.rsrpTextView).text = rsrp.toString()

                rsrq = m.cellSignalStrength.rsrq
                context.findViewById<TextView>(R.id.rsrqTextView).text = rsrq.toString()

                rssi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    m.cellSignalStrength.rssi
                } else {
                    -1
                }
                context.findViewById<TextView>(R.id.rssiTextView).text = rssi.toString()

                rssnr = m.cellSignalStrength.rssnr
                context.findViewById<TextView>(R.id.rssnrTextView).text = rssnr.toString()

                earfcn = m.cellIdentity.earfcn
                context.findViewById<TextView>(R.id.earfcnTextView).text = earfcn.toString()

                pci = m.cellIdentity.pci
                context.findViewById<TextView>(R.id.pciTextView).text = pci.toString()

                list = listOf(rsrp, rsrq, rssi, rssnr, earfcn, pci)
            }
        }
    }

    companion object {
        var rsrp = 0
        var rsrq = 0
        var rssi = 0
        var rssnr = 0
        var earfcn = 0
        var pci = 0
    }
}