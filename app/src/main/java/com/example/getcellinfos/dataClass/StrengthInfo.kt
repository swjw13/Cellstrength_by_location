package com.example.getcellinfos.dataClass

data class strengthInfo(
    var rssi: Int = -1,
    var rsrp: Int? = -1,
    var rsrq: Int? = -1,
    var timeStamp: Long? = -1
)