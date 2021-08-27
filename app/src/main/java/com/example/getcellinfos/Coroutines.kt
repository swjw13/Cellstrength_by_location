package com.example.getcellinfos

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class Coroutines(private val threadType: String): CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = when(threadType){
            "main" -> Dispatchers.Main + job
            "io" -> Dispatchers.IO + job
            else -> Dispatchers.Default + job
        }

    fun launch(activation: () -> Unit){
        launch(coroutineContext){
            withContext(Dispatchers.Main){

            }
        }
    }
}