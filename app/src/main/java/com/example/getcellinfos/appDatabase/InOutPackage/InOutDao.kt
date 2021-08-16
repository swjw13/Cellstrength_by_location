package com.example.getcellinfos.appDatabase.InOutPackage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface InOutDao {

    @Insert
    fun insertInOutFindData(station: InOutData)

    @Query("DELETE FROM inoutdata")
    fun clearTable()
}