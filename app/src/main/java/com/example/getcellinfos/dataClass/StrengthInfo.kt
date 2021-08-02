package com.example.getcellinfos.dataClass

data class StrengthInfo(
    var rssi: Int = -1,
    var rsrp: Int? = -1,
    var rsrq: Int? = -1,
    var timeStamp: Long? = -1
)