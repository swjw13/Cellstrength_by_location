package com.example.getcellinfos.appDatabase.Instance

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.room.Room
import com.ajts.androidmads.library.SQLiteToExcel
import java.io.File

abstract class DatabaseManager {

    // 각 매니저마다 생성하는 AppDatabase 가 같기 때문에 상위 Factory 에서 생성을 해 준다(같은 excel 로 관리)
    // instance 객체는 상속받은 manager 내에서만 접근 가능
    protected fun getDatabaseInstance(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "CellInfo").build()
    }

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