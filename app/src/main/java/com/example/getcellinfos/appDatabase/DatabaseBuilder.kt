package com.example.getcellinfos.appDatabase

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.ajts.androidmads.library.SQLiteToExcel
import com.example.getcellinfos.appDatabase.Stations.StationDatabaseManager
import com.example.getcellinfos.appDatabase.logs.LogDatabaseManager
import java.io.File

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