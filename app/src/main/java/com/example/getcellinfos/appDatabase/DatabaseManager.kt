package com.example.getcellinfos.appDatabase

interface DatabaseManager {
    fun insert(item: DatabaseDto)

    fun deleteAll()
}