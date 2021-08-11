package com.example.getcellinfos.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("location")
    fun getStationInfo(
        @Query("enbId") enbId: Int,
        @Query("cellNum") cellNum: Int
    ): Call<RetrofitDto>
}