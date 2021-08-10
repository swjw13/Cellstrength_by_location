package com.example.getcellinfos.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClass {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://3.35.196.165.8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(retrofitService::class.java)
}