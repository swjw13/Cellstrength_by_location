package com.example.getcellinfos.appDatabase.InOutPackage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.getcellinfos.appDatabase.Instance.DatabaseDto
import java.util.*

@Entity
data class InOutData(
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name = "Date") val date: String,
    @ColumnInfo(name = "Time") val time: String,
    @ColumnInfo(name = "Latitude") val latitude: String,
    @ColumnInfo(name = "Longitude") val longitude: String,
    @ColumnInfo(name = "Altitude") val altitude: String,
    @ColumnInfo(name = "Rsrp") val rsrp: Int,
    @ColumnInfo(name = "Rsrq") val rsrq: Int,
    @ColumnInfo(name = "Rssi") val rssi: Int,
    @ColumnInfo(name = "Rssnr") val rssnr: Int,
    @ColumnInfo(name = "Pci") val pci: Int,
    @ColumnInfo(name = "Earfcn") val earfcn: Int,
    @ColumnInfo(name = "NeighborCell") val neighborCell: Int,
    @ColumnInfo(name = "wifi_mostPowerful") var wifi_powerful_strength: Int? = 0,
    @ColumnInfo(name = "satellite_strengths") var satellite_strengths: String? = "",
    @ColumnInfo(name = "nr_ssRsrp") var ssrsrp: Int? = 0,
    @ColumnInfo(name = "nr_ssRsrq") var ssrsrq: Int? = 0,
    @ColumnInfo(name = "nr_ssSinr") var sssinr: Int? = 0,
    @ColumnInfo(name = "In / Out") var inOut: String? = ""
    ): DatabaseDto