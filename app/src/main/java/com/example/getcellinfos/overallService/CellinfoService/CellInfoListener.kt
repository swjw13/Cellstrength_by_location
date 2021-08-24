package com.example.getcellinfos.overallService.CellinfoService

import android.annotation.SuppressLint
import android.os.Build
import android.telephony.CellInfo
import android.telephony.CellInfoLte
import android.telephony.PhoneStateListener
import com.example.getcellinfos.otherCellList.OtherCells
import com.example.getcellinfos.retrofit.retrofitAnswer.RetrofitDto
import com.example.getcellinfos.retrofit.retrofitInstance.RetrofitClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CellInfoListener(
    val updateView: ((MutableList<Int>) -> Unit),
    val updateAdapter: (MutableList<OtherCells>) -> Unit,
    val updateMap: (Float, Float) -> Unit,
    val callStation: Boolean = true
) : PhoneStateListener() {

    var ci = 0
    var enbId_tmp = 0
    var cellNum_tmp = 0
    var cell_lat = 0F
    var cell_lon = 0F
    private val retrofitClass = RetrofitClass()
    var otherCellList = mutableListOf<OtherCells>()

    private var cellInfoList = mutableListOf(0, 0, 0, 0, 0)

    @SuppressLint("SetTextI18n")
    override fun onCellInfoChanged(cellInfo: MutableList<CellInfo>?) {
        super.onCellInfoChanged(cellInfo)

        otherCellList = mutableListOf()

        if (cellInfo != null) {
            cellInfoList[2] = cellInfo.size

            for (m in cellInfo) {
                if (m is CellInfoLte) {
                    val otherCellInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        OtherCells(
                            rsrp = m.cellSignalStrength.rsrp,
                            rsrq = m.cellSignalStrength.rsrq,
                            rssi = m.cellSignalStrength.rssi,
                            rssnr = m.cellSignalStrength.rssnr,
                            earfcn = m.cellIdentity.earfcn,
                            pci = m.cellIdentity.pci
                        )
                    } else {
                        OtherCells(
                            rsrp = m.cellSignalStrength.rsrp,
                            rsrq = m.cellSignalStrength.rsrq,
                            rssi = 0,
                            rssnr = m.cellSignalStrength.rssnr,
                            earfcn = m.cellIdentity.earfcn,
                            pci = m.cellIdentity.pci
                        )
                    }

                    otherCellList.add(otherCellInfo)

                    if (m.isRegistered) {
                        cellInfoList[0] = m.cellIdentity.earfcn

                        cellInfoList[1] = m.cellIdentity.pci

                        ci = m.cellIdentity.ci
                        enbId_tmp = (ci and bit_enbId) shr 8
                        cellNum_tmp = (ci and bit_cellnum) shr 2
                        if (enbId_tmp != cellInfoList[3] || cellNum_tmp != cellInfoList[4]) {
                            getStationInfo(enbId_tmp, cellNum_tmp)
                        }
                        cellInfoList[3] = (ci and bit_enbId) shr 8
                        cellInfoList[4] = (ci and bit_cellnum) shr 2
                    }
                }
            }
            updateAdapter(otherCellList)
            updateView(cellInfoList)
        }
    }

    private fun getStationInfo(enbId: Int, cellNum: Int) {
        if(callStation) {
            retrofitClass.getInstance().getStationInfo(
                enbId = enbId, cellNum = cellNum
            ).enqueue(object : Callback<RetrofitDto> {
                override fun onResponse(call: Call<RetrofitDto>, response: Response<RetrofitDto>) {
                    if (response.isSuccessful) {
                        cell_lat = response.body()?.result?.eqp_lat ?: 0F
                        cell_lon = response.body()?.result?.eqp_lon ?: 0F
                        updateMap(cell_lat, cell_lon)
                    }
                }

                override fun onFailure(call: Call<RetrofitDto>, t: Throwable) {
                }
            })
        }
    }

    companion object {
        const val bit_enbId = 0b1111111111111111111100000000
        const val bit_cellnum = 0b11111100
    }

    fun getCellList(): MutableList<Int> {
        return cellInfoList
    }
}