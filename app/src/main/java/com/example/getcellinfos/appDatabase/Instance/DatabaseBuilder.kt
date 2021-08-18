package com.example.getcellinfos.appDatabase.Instance

import android.content.Context
import com.example.getcellinfos.appDatabase.InOutPackage.InOutDatabaseManager
import com.example.getcellinfos.appDatabase.Stations.StationDatabaseManager
import com.example.getcellinfos.appDatabase.logs.LogDatabaseManager

class DatabaseBuilder(val context: Context){

    fun getInstance(databaseName: String): DatabaseManager?{
        return when(databaseName){
            "log" -> LogDatabaseManager(context)
            "station" -> StationDatabaseManager(context)
            "inout" -> InOutDatabaseManager(context)
            else -> null
        }
    }
}