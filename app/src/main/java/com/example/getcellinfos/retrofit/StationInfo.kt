package com.example.getcellinfos.retrofit

data class StationInfo(
    val eqp_no: String = "",
    val eqp_name: String = "",
    val addr: String = "",
    val new_addr: String = "",
    val eqp_type: String = "",
    val enb_id: String = "",
    val pnu_code: String = "",
    val eqp_lat: Double,
    val eqp_lon: Double,
    val cell_num: String
)
