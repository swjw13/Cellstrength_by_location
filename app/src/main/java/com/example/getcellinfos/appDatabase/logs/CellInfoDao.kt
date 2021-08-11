package com.example.getcellinfos.appDatabase.logs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CellInfoDao {
    @Insert
    fun insert(cellInfo: CellInfo)

    @Query("DELETE FROM cellinfo")
    fun clearTable()
}