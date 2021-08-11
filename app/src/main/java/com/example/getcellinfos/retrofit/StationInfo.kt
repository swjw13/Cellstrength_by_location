package com.example.getcellinfos.retrofit

import java.io.Serializable

data class StationInfo(
    var eqp_no: String? = "",
    var eqp_name: String? = "",
    var addr: String? = "",
    var new_addr: String? = "",
    var eqp_type: String? = "",
    var enb_id: String? = "",
    var pnu_code: String? = "",
    var eqp_lat: Float? = 0F,
    var eqp_lon: Float? = 0F,
    var cell_num: String? = ""
) : Serializable