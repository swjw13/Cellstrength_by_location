package com.example.getcellinfos.retrofit

data class StationInfo(
    var eqp_no: String,
    var eqp_name: String,
    var addr: String,
    var new_addr: String,
    var eqp_type: String,
    var enb_id: String,
    var pnu_code: String,
    var eqp_lat: Double,
    var eqp_lon: Double,
    var cell_num: String
)