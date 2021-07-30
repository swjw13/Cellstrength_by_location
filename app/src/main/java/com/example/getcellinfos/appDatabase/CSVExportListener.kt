package com.example.getcellinfos.appDatabase

import android.content.Context
import android.widget.Toast
import com.ajts.androidmads.library.SQLiteToExcel

class CSVExportListener(private val context: Context) : SQLiteToExcel.ExportListener {
    override fun onStart() {
        Toast.makeText(context, "file create start", Toast.LENGTH_SHORT).show()
    }

    override fun onCompleted(filePath: String?) {
        Toast.makeText(context, "file create success", Toast.LENGTH_SHORT).show()
    }

    override fun onError(e: java.lang.Exception?) {
        Toast.makeText(
            context,
            "file create Failure, " + e?.message,
            Toast.LENGTH_SHORT
        ).show()
    }
}