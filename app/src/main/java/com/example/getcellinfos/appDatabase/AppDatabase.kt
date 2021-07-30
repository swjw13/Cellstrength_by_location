package com.example.getcellinfos.appDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.getcellinfos.dataClass.CellInfo

@Database(entities = [CellInfo::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun cellInfoDto() : CellInfoDao
}