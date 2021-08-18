package com.example.getcellinfos.appDatabase.Stations

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.getcellinfos.appDatabase.Instance.DatabaseDto

@Entity
data class StationInfoDatabaseDTO(
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name = "eqp_no") var eqp_no: String? = "",
    @ColumnInfo(name = "eqp_name") var eqp_name: String? = "",
    @ColumnInfo(name = "addr") var addr: String? = "",
    @ColumnInfo(name = "new_addr") var new_addr: String? = "",
    @ColumnInfo(name = "eqp_type") var eqp_type: String? = "",
    @ColumnInfo(name = "enb_id") var enb_id: String? = "",
    @ColumnInfo(name = "pnu_code") var pnu_code: String? = "",
    @ColumnInfo(name = "eqp_lat") var eqp_lat: Float? = 0F,
    @ColumnInfo(name = "eqp_lon") var eqp_lon: Float? = 0F,
    @ColumnInfo(name = "cell_num") var cell_num: String? = ""
): DatabaseDto