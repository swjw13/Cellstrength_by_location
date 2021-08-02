package com.example.getcellinfos.appDatabase

import com.ajts.androidmads.library.SQLiteToExcel

class CSVExportListener(private val showToast: (String) -> Unit) : SQLiteToExcel.ExportListener {
    override fun onStart() {
        showToast("file create start")
    }

    override fun onCompleted(filePath: String?) {
        showToast("file create success")
    }

    override fun onError(e: java.lang.Exception?) {
        showToast("file create Failure, " + e?.localizedMessage)
    }
}