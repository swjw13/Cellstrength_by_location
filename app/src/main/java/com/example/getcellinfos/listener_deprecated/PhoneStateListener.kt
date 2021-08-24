package com.example.getcellinfos.listener_deprecated

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.telephony.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.getcellinfos.R
import com.example.getcellinfos.otherCellList.OtherCells
import com.example.getcellinfos.retrofit.retrofitInstance.RetrofitClass
import com.example.getcellinfos.retrofit.retrofitAnswer.RetrofitDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class phoneStateListener(val context: Activity, val updateAdapter: (MutableList<OtherCells>) -> Unit, val updateMap: (Float, Float) -> Unit) :
    PhoneStateListener() {

    var list = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    var otherCellList = mutableListOf<OtherCells>()
    var ci = 0
    var enbId_tmp = 0
    var cellNum_tmp = 0
    var cell_lat = 0F
    var cell_lon = 0F
    private val retrofitClass = RetrofitClass()

    @SuppressLint("SetTextI18n")
    override fun onSignalStrengthsChanged(signalStrength: SignalStrength?) {
        super.onSignalStrengthsChanged(signalStrength)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            signalStrength?.cellSignalStrengths?.forEach {
                if (it is CellSignalStrengthLte) {
                    list[0] = it.rsrp
                    list[1] = it.rsrq
                    list[2] = it.rssi
                    list[3] = it.rssnr

                    context.findViewById<TextView>(R.id.rsrpTextView).text = "rsrp: ${list[0]}"
                    context.findViewById<TextView>(R.id.rsrqTextView).text = "rsrq: ${list[1]}"
                    context.findViewById<TextView>(R.id.rssiTextView).text = "rssi: ${list[2]}"
                    context.findViewById<TextView>(R.id.rssnrTextView).text = "rssnr: ${list[3]}"
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCellInfoChanged(cellInfo: MutableList<CellInfo>?) {
        super.onCellInfoChanged(cellInfo)

        otherCellList = mutableListOf()

        if (cellInfo != null) {

            list[6] = cellInfo.size
            context.findViewById<TextView>(R.id.neighborcellTextView).text = list[6].toString()

            for (m in cellInfo) {
                if (m is CellInfoLte) {
                    val otherCellInfo = OtherCells(
                        m.cellSignalStrength.rsrp,
                        m.cellSignalStrength.rsrq,
                        m.cellSignalStrength.rssi,
                        m.cellSignalStrength.rssnr,
                        m.cellIdentity.earfcn,
                        m.cellIdentity.pci
                    )

                    otherCellList.add(otherCellInfo)

                    if (m.isRegistered) {
                        list[4] = m.cellIdentity.earfcn
                        context.findViewById<TextView>(R.id.earfcnTextView).text =
                            "earfcn: ${list[4]}"

                        list[5] = m.cellIdentity.pci
                        context.findViewById<TextView>(R.id.pciTextView).text = "pci: ${list[5]}"

                        ci = m.cellIdentity.ci
                        enbId_tmp = (ci and bit_enbId) shr 8
                        cellNum_tmp = (ci and bit_cellnum) shr 2
                        if(enbId_tmp != list[7] || cellNum_tmp != list[8]){
                            getStationInfo(enbId_tmp,cellNum_tmp)
                        }
                        list[7] = (ci and bit_enbId) shr 8
                        list[8] = (ci and bit_cellnum) shr 2
                    }
                }
            }
            updateAdapter(otherCellList)
        }
    }
    private fun getStationInfo(enbId: Int, cellNum: Int) {

        retrofitClass.getInstance().getStationInfo(
            enbId = enbId, cellNum = cellNum
        ).enqueue(object: Callback<RetrofitDto> {
            override fun onResponse(call: Call<RetrofitDto>, response: Response<RetrofitDto>) {
                if(response.isSuccessful){
                    cell_lat = response.body()?.result?.eqp_lat ?: 0F
                    cell_lon = response.body()?.result?.eqp_lon ?: 0F
                    updateMap(cell_lat, cell_lon)
                } else{
                    Toast.makeText(context, "기지국 정보 가져오기에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RetrofitDto>, t: Throwable) {
                Toast.makeText(context, "기지국 정보 가져오기에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object{
        const val bit_enbId = 0b1111111111111111111100000000
        const val bit_cellnum = 0b11111100
    }
}