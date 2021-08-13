package com.example.getcellinfos.appDatabase

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.ajts.androidmads.library.SQLiteToExcel
import java.io.File

abstract class DatabaseManager {
    abstract fun insert(item: DatabaseDto)

    abstract fun deleteAll()

    fun exportCSV(context: Context) {
        val dir =
            Environment.getExternalStorageDirectory().toString() + File.separator + "CellInfo/CSV/"
        if (!File(dir).exists()) {
            File(dir).mkdirs()
        }

        val time = System.currentTimeMillis()

        val sqlToExcel = SQLiteToExcel(context, "CellInfo", dir)
        sqlToExcel.exportAllTables("$time.csv", CSVExportListener { text ->
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        })
    }
}