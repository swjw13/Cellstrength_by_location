package com.example.getcellinfos.retrofit

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RetrofitDto(
    var result: StationInfo? = null
) : Serializable