package com.example.getcellinfos.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.getcellinfos.R

class phoneStateAll : AppCompatActivity() {

    private val showAllTextView: TextView by lazy {
        findViewById(R.id.allStateTextView)
    }
    private var string = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_state_all)

        val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        addString(telephonyManager.dataState.toString())
        addString(telephonyManager.simState.toString())
        addString(telephonyManager.networkOperator)



        Log.d("PHONE", "getCallState :" + telephonyManager.callState)
        Log.d("PHONE", "getDataState :" + telephonyManager.dataState)
        Log.d("PHONE", "getNETWORKCountryIso :" + telephonyManager.networkCountryIso)
        Log.d("PHONE", "getSimCountryIso :" + telephonyManager.simCountryIso)
        Log.d("PHONE", "getNetworkOperator :" + telephonyManager.networkOperator)
        Log.d("PHONE", "getSimOperator :" + telephonyManager.simOperator)
        Log.d("PHONE", "getNetworkOperatorName :" + telephonyManager.networkOperatorName)
        Log.d("PHONE", "getSimOperatorName :" + telephonyManager.simOperatorName)
        Log.d("PHONE", "getPhoneType :" + telephonyManager.phoneType)
        Log.d("PHONE", "getSimState :" + telephonyManager.simState)

        showAllTextView.text = string
    }

    private fun addString(string: String) {
        this.string += string
        this.string += '\n'
    }
}