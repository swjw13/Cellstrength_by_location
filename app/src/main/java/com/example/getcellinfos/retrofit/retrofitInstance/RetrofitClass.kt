package com.example.getcellinfos.retrofit.retrofitInstance

import com.example.getcellinfos.retrofit.RetrofitService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable

class RetrofitClass: Serializable {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://3.35.196.165:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: RetrofitService = retrofit.create(RetrofitService::class.java)

    fun getInstance(): RetrofitService = service
}