package com.example.getcellinfos.dataClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CellInfo(
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name = "Date") val date: String,
    @ColumnInfo(name = "Time") val time: String,
    @ColumnInfo(name = "Latitude") val latitude: Double,
    @ColumnInfo(name = "Longitude") val longitude: Double,
    @ColumnInfo(name = "Altitude") val altitude: Double,
    @ColumnInfo(name = "Rsrp") val rsrp: Int,
    @ColumnInfo(name = "Rsrq") val rsrq: Int,
    @ColumnInfo(name = "Rssi") val rssi: Int,
    @ColumnInfo(name = "Rssnr") val rssnr: Int,
    @ColumnInfo(name = "Pci") val pci: Int,
    @ColumnInfo(name = "Earfcn") val earfcn: Int,
    @ColumnInfo(name = "NeighborCell") val neighborCell: Int,
    @ColumnInfo(name = "Memo") val memo: String
)