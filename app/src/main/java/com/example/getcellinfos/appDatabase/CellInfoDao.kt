package com.example.getcellinfos.appDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CellInfoDao {
    @Insert
    fun insertLog(cellInfo: CellInfo)

    @Query("DELETE FROM cellinfo")
    fun clearTable()
}