package com.example.getcellinfos.appDatabase

import android.content.Context
import com.example.getcellinfos.appDatabase.Stations.StationDatabaseManager
import com.example.getcellinfos.appDatabase.logs.LogDatabaseManager

class DatabaseBuilder(val context: Context){

    fun getInstance(databaseName: String): DatabaseManager?{
        return if(databaseName == "log"){
            LogDatabaseManager(context)
        } else if (databaseName == "station"){
            StationDatabaseManager(context)
        } else{
            null
        }
    }
}