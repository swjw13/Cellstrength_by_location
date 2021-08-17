package com.example.getcellinfos.retrofit.retrofitCallback

import android.content.Context
import android.widget.Toast
import com.example.getcellinfos.appDatabase.Instance.DatabaseBuilder
import com.example.getcellinfos.appDatabase.Stations.StationInfoDatabaseDTO
import com.example.getcellinfos.retrofit.retrofitAnswer.RetrofitDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StationInfoCallback(val context: Context): Callback<RetrofitDto> {

    private val database = DatabaseBuilder(context).getInstance("station")!!

    override fun onResponse(call: Call<RetrofitDto>, response: Response<RetrofitDto>) {
        if (response.isSuccessful) {
            val currentStation = response.body()?.result
            val stationInfo = StationInfoDatabaseDTO(
                uid = null,
                eqp_no = currentStation?.eqp_no,
                eqp_name = currentStation?.eqp_name,
                addr = currentStation?.addr,
                new_addr = currentStation?.new_addr,
                eqp_type = currentStation?.eqp_type,
                enb_id = currentStation?.enb_id,
                pnu_code = currentStation?.pnu_code,
                eqp_lat = currentStation?.eqp_lat,
                eqp_lon = currentStation?.eqp_lon,
                cell_num = currentStation?.cell_num
            )

            database.insert(stationInfo)
            Toast.makeText(context, "Data 등록 완료", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(context, "No Data Income", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(call: Call<RetrofitDto>, t: Throwable) {
        Toast.makeText(context, "Communication Error", Toast.LENGTH_SHORT).show()
    }
}
