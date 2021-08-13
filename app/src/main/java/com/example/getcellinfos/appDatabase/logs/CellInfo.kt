package com.example.getcellinfos.appDatabase.logs

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.getcellinfos.appDatabase.DatabaseDto

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
    @ColumnInfo(name = "Memo") val memo: String,
    @ColumnInfo(name = "Other Cell Rsrp") val other_cell_rsrp: String,
    @ColumnInfo(name = "Other Cell Rsrq") val other_cell_rsrq: String,
    @ColumnInfo(name = "Other Cell Rssi") val other_cell_rssi: String,
    @ColumnInfo(name = "Other Cell Rssnr") val other_cell_rssnr: String,
    @ColumnInfo(name = "Other Cell Earfcn") val other_cell_earfcn: String,
    @ColumnInfo(name = "Other Cell Pci") val other_cell_Pci: String
): DatabaseDto