package com.example.getcellinfos.overallService

import android.annotation.SuppressLint
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

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

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTimeFromFormat(pattern: String): String {
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat(pattern)
        return dateFormat.format(date)
    }
}