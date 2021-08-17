package com.example.getcellinfos.appDatabase.InOutPackage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.getcellinfos.appDatabase.Instance.DatabaseDto

@Entity
data class InOutData(
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name = "In / Out") var inOut: String? = "",
    @ColumnInfo(name = "wifi_mostPowerful") var wifi_powerful_strength: Int? = 0,
    @ColumnInfo(name = "satellite_strengths") var satellite_strengths: String? = "",
    @ColumnInfo(name = "nr_ssRsrp") var ssrsrp: Int? = 0,
    @ColumnInfo(name = "nr_ssRsrq") var ssrsrq: Int? = 0,
    @ColumnInfo(name = "nr_ssSinr") var sssinr: Int? = 0,
): DatabaseDto