package com.example.getcellinfos.appDatabase.InOutPackage

import android.content.Context
import android.widget.Toast
import com.example.getcellinfos.appDatabase.DatabaseDto
import com.example.getcellinfos.appDatabase.DatabaseManager
import com.example.getcellinfos.appDatabase.logs.CellInfo
import java.lang.Exception

class InOutDatabaseManager(val context: Context): DatabaseManager() {

    private val database = getDatabaseInstance(context)

    override fun insert(item: DatabaseDto) {

        // if thread is allive, thread can be collide
        // To avoid, interrupt alive thread
        // TODO: Thread와 관련해 다른 알고리즘 적용시키기
        if(Thread.currentThread().isAlive){
            Thread.currentThread().interrupt()
        }

        Thread {
            try {
                database.inOutDto().insertInOutFindData(item as InOutData)
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
                database.inOutDto().clearTable()
            } catch (e: Exception){
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}