package com.example.getcellinfos.overallService

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("MissingPermission")
class OverAllClass(val context: Context) {
    private var mLocationService: LocationAll? = null
    private var mCellService: CellAll? = null

    fun locationService(): LocationAll {
        if (mLocationService == null) {
            mLocationService = LocationAll(context)
        }
        return mLocationService as LocationAll
    }

    fun cellService(): CellAll{
        if(mCellService == null){
            mCellService = CellAll(context)
        }
        return mCellService as CellAll
    }
}