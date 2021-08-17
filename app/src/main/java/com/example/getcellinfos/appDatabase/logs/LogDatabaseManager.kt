package com.example.getcellinfos.appDatabase.logs

import android.content.Context
import android.widget.Toast
import com.example.getcellinfos.appDatabase.Instance.DatabaseDto
import com.example.getcellinfos.appDatabase.Instance.DatabaseManager
import java.lang.Exception

class LogDatabaseManager(val context: Context) : DatabaseManager() {

    // get AppDatabase: base Database Instance
    private val database = getDatabaseInstance(context)

    override fun insert(item: DatabaseDto) {
        if(Thread.currentThread().isAlive){
            Thread.currentThread().interrupt()
        }
        Thread {
            try {
                database.cellInfoDto().insertCellularLog(item as CellInfo)
            } catch (e: Exception) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    override fun deleteAll() {
        if(Thread.currentThread().isAlive){
            Thread.currentThread().interrupt()
        }
        Thread{
            try{
                database.cellInfoDto().clearTable()
            } catch (e: Exception){
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}