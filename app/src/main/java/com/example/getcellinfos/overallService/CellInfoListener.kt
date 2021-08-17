package com.example.getcellinfos.overallService

import android.app.Activity
import android.os.Build
import android.telephony.CellInfo
import android.telephony.CellInfoLte
import android.telephony.PhoneStateListener
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.getcellinfos.R
import com.example.getcellinfos.otherCellList.OtherCells
import com.example.getcellinfos.retrofit.retrofitAnswer.RetrofitDto
import com.example.getcellinfos.retrofit.retrofitInstance.RetrofitClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.Q)
class CellInfoListener(val context: Activity, val updateAdapter: (MutableList<OtherCells>) -> Unit, val updateMap: (Float, Float) -> Unit): PhoneStateListener() {

    var ci = 0
    var enbId_tmp = 0
    var cellNum_tmp = 0
    var cell_lat = 0F
    var cell_lon = 0F
    private val retrofitClass = RetrofitClass()
    var otherCellList = mutableListOf<OtherCells>()

    private val cellInfoList = mutableListOf(0,0,0,0,0)

    override fun onCellInfoChanged(cellInfo: MutableList<CellInfo>?) {
        super.onCellInfoChanged(cellInfo)

        otherCellList = mutableListOf()

        if (cellInfo != null) {
            cellInfoList[2] = cellInfo.size
            context.findViewById<TextView>(R.id.neighborcellTextView).text = cellInfoList[2].toString()

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
                        cellInfoList[0] = m.cellIdentity.earfcn
                        context.findViewById<TextView>(R.id.earfcnTextView).text = "earfcn: ${cellInfoList[0]}"

                        cellInfoList[1] = m.cellIdentity.pci
                        context.findViewById<TextView>(R.id.pciTextView).text = "pci: ${cellInfoList[1]}"

                        ci = m.cellIdentity.ci
                        enbId_tmp = (ci and bit_enbId) shr 8
                        cellNum_tmp = (ci and bit_cellnum) shr 2
                        if(enbId_tmp != cellInfoList[3] || cellNum_tmp != cellInfoList[4]){
                            getStationInfo(enbId_tmp,cellNum_tmp)
                        }
                        cellInfoList[3] = (ci and bit_enbId) shr 8
                        cellInfoList[4] = (ci and bit_cellnum) shr 2
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

    fun getCellList(): MutableList<Int>{
        return cellInfoList
    }
}