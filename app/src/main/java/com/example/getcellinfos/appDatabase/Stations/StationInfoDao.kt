package com.example.getcellinfos.appDatabase.Stations

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StationInfoDao {

    @Insert
    fun insert(station: StationInfoDatabaseDTO)

    @Query("DELETE FROM stationinfodatabasedto")
    fun clearTable()
}