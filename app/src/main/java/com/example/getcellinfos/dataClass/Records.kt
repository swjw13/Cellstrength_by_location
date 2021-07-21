package com.example.getcellinfos.dataClass

data class records(
    var latitude: Double? = null,
    var longitude: Double? = null,
    var altitude: Double? = null,
    var mobMCC: Int? = null,
    var mobMNC: Int? = null,
    var cid: Int? = null,
    var lac: Int? = null,
    var rssi: Int? = null,
    var rsrp: Int? = null,
    var rsrq: Int? = null
)