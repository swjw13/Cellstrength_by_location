package com.example.getcellinfos.overallService

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("MissingPermission")
class OverAllClass(val context: Context) {
    private var mLocationService: LocationAll? = null
    private var mStrengthService: CellAll? = null

    fun locationService(): LocationAll {
        if (mLocationService == null) {
            mLocationService = LocationAll(context)
        }
        return mLocationService as LocationAll
    }

    fun cellService(): CellAll{
        if(mStrengthService == null){
            mStrengthService = CellAll(context)
        }
        return mStrengthService as CellAll
    }
}