package com.example.getcellinfos.appDatabase.Stations

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import com.example.getcellinfos.appDatabase.AppDatabase
import com.example.getcellinfos.appDatabase.DatabaseDto
import com.example.getcellinfos.appDatabase.DatabaseManager
import java.lang.Exception

class StationDatabaseManager(val context: Context): DatabaseManager() {
    private val database =
        Room.databaseBuilder(context, AppDatabase::class.java, "CellInfo").build()

    override fun insert(item: DatabaseDto) {

        if(Thread.currentThread().isAlive){
            Thread.currentThread().interrupt()
        }

        Thread {
            try {
                database.stationInfoDto().insertStation(item as StationInfoDatabaseDTO)
                Log.d("jae", item.toString())
            } catch (e: Exception){
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    override fun deleteAll() {
        if(Thread.currentThread().isAlive){
            Thread.currentThread().interrupt()
        }
        Thread {
            try {
                database.stationInfoDto().clearTable()
            } catch (e: Exception){
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}