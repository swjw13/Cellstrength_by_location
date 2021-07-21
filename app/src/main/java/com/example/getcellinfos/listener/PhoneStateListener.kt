package com.example.getcellinfos.listener

import android.app.Activity
import android.os.Build
import android.telephony.*
import android.telephony.cdma.CdmaCellLocation
import android.telephony.gsm.GsmCellLocation
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.getcellinfos.R
import com.example.getcellinfos.dataClass.locationInfo
import com.example.getcellinfos.dataClass.strengthInfo

class phoneStateListener(val context: Activity) : PhoneStateListener() {

    var strengths: MutableList<strengthInfo> = mutableListOf()
    var locations: MutableList<locationInfo> = mutableListOf()
    private val strengthTextView: TextView = context.findViewById(R.id.cellSignalStrengthInfo)
    private val locationTextView: TextView = context.findViewById(R.id.cellLocationInfo)

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onSignalStrengthsChanged(signalStrength: SignalStrength?) {
        super.onSignalStrengthsChanged(signalStrength)

        val result = extractInfoFromStrength(signalStrength)

        if (result.isNotEmpty()) {
            strengths.add(
                strengthInfo(
                    result[0],  // rssi
                    result[1],  // rsrp
                    result[2],  // rsrq
                    signalStrength?.timestampMillis
                )
            )
            strengthTextView.text = strengths[strengths.size - 1].toString()
//            strengthTextView.text = signalStrength.toString()
        } else {
            strengthTextView.text = "No signal is detected"
            locationTextView.text = ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun extractInfoFromStrength(signalStrength: SignalStrength?): List<Int> {

        if ((signalStrength != null) && (signalStrength.cellSignalStrengths.size > 0)) {
            val sStrength = signalStrength.cellSignalStrengths[0].toString()

            val list = sStrength.split(" ")

            val rssi = list[1].substring(5, list[1].length).toInt()
            val rsrp = signalStrength.cellSignalStrengths.get(0)?.dbm ?: -1
            val rsrq = list[3].substring(5, list[3].length).toInt()

            return listOf(rssi, rsrp, rsrq)
        } else {
            return listOf()
        }
    }

    override fun onCellLocationChanged(location: CellLocation?) {
        super.onCellLocationChanged(location)
        var cid = 0
        var lac = 0

        if (location != null) {
            if (location is GsmCellLocation) {
                cid = location.cid
                lac = location.lac
            } else if (location is CdmaCellLocation) {
                cid = location.baseStationId
                lac = location.systemId
            }
            locations.add(locationInfo(cid, lac))
            locationTextView.text = locations[locations.size - 1].toString()
        } else {
            locationTextView.text = "Unable to detect Location"
        }
    }

    override fun onServiceStateChanged(serviceState: ServiceState?) {
        super.onServiceStateChanged(serviceState)

        if (serviceState?.state != ServiceState.STATE_IN_SERVICE) {
            Toast.makeText(context, "Service is not available", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDataConnectionStateChanged(state: Int, networkType: Int) {
        super.onDataConnectionStateChanged(state, networkType)

        context.findViewById<TextView>(R.id.networkStateTextView).text = when (state) {
            0 -> {
                "Data Disconnected"
            }
            2 -> {
                "Data Connected: $networkType"
            }
            else -> {
                "else"
            }
        }
    }
}