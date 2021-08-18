package com.example.getcellinfos.appDatabase.Instance

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.getcellinfos.appDatabase.InOutPackage.InOutDao
import com.example.getcellinfos.appDatabase.InOutPackage.InOutData
import com.example.getcellinfos.appDatabase.Stations.StationInfoDao
import com.example.getcellinfos.appDatabase.logs.CellInfo
import com.example.getcellinfos.appDatabase.logs.CellInfoDao
import com.example.getcellinfos.appDatabase.Stations.StationInfoDatabaseDTO

@Database(entities = [CellInfo::class, StationInfoDatabaseDTO::class, InOutData::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun cellInfoDto() : CellInfoDao

    abstract fun stationInfoDto(): StationInfoDao

    abstract fun inOutDto(): InOutDao
}