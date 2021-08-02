package com.example.getcellinfos.dataClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CellInfo(
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
    @ColumnInfo(name = "Memo") val memo: String
)