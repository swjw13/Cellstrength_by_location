package com.example.getcellinfos.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClass {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://3.35.196.165:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()



    private val service: RetrofitService = retrofit.create(RetrofitService::class.java)

    fun getInstance():RetrofitService = service
}