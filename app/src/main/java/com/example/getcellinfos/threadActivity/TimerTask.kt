package com.example.getcellinfos.threadActivity

import java.util.*

class timerTask(
    private val updateDB: () -> Unit
) : TimerTask() {
    override fun run() {
        updateDB()
    }
}