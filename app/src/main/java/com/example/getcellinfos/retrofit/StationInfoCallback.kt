package com.example.getcellinfos.retrofit

import android.content.Context
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StationInfoCallback(val context: Context): Callback<RetrofitDto> {
    override fun onResponse(call: Call<com.example.getcellinfos.retrofit.RetrofitDto>, response: Response<com.example.getcellinfos.retrofit.RetrofitDto>) {
        if (response.isSuccessful) {
            val currentStation = response.body()?.result

        } else {
            Toast.makeText(context, "No Data Income", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(call: Call<com.example.getcellinfos.retrofit.RetrofitDto>, t: Throwable) {
        Toast.makeText(context, "Communication Error", Toast.LENGTH_SHORT).show()
    }
}