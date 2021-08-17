package com.example.getcellinfos.retrofit

import com.example.getcellinfos.retrofit.retrofitAnswer.RetrofitDto
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