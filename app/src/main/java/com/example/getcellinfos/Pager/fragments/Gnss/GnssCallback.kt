package com.example.getcellinfos.Pager.fragments.Gnss

import android.location.GnssStatus

class GnssCallback(val updateView: () -> Unit) : GnssStatus.Callback() {

    var gpsSateliteList: MutableList<Float> = mutableListOf()
    var sbasSateliteList: MutableList<Float> = mutableListOf()
    var glonassSateliteList: MutableList<Float> = mutableListOf()
    var gzssSateliteList: MutableList<Float> = mutableListOf()
    var beidouSateliteList: MutableList<Float> = mutableListOf()
    var galileoSateliteList: MutableList<Float> = mutableListOf()
    var irnssSateliteList: MutableList<Float> = mutableListOf()
    var unknownSateliteList: MutableList<Float> = mutableListOf()

    var strength_total = 0F
    var size_total = 0
    var size_nonZero = 0
    var inOut_strength = ""
    var inOut_nonZero = ""

    var satCount = 0

    override fun onSatelliteStatusChanged(status: GnssStatus) {
        super.onSatelliteStatusChanged(status)

        gpsSateliteList = mutableListOf()
        gpsSateliteList = mutableListOf()
        sbasSateliteList = mutableListOf()
        glonassSateliteList = mutableListOf()
        gzssSateliteList = mutableListOf()
        beidouSateliteList = mutableListOf()
        galileoSateliteList = mutableListOf()
        irnssSateliteList = mutableListOf()
        unknownSateliteList = mutableListOf()

        strength_total = 0F
        size_total = 0
        size_nonZero = 0

        satCount = status.satelliteCount

        for (i in 0 until satCount - 1) {
            when (status.getConstellationType(i)) {
                GnssStatus.CONSTELLATION_GPS -> {
                    gpsSateliteList.add(status.getCn0DbHz(i))

                    if (status.getCn0DbHz(i) != 0F) {
                        strength_total += status.getCn0DbHz(i)
                        size_nonZero += 1
                    }
                    size_total += 1
                }
                GnssStatus.CONSTELLATION_GLONASS -> glonassSateliteList.add(status.getCn0DbHz(i))
                GnssStatus.CONSTELLATION_SBAS -> sbasSateliteList.add(status.getCn0DbHz(i))
                GnssStatus.CONSTELLATION_BEIDOU -> beidouSateliteList.add(status.getCn0DbHz(i))
                GnssStatus.CONSTELLATION_GALILEO -> galileoSateliteList.add(status.getCn0DbHz(i))
                GnssStatus.CONSTELLATION_IRNSS -> irnssSateliteList.add(status.getCn0DbHz(i))
                GnssStatus.CONSTELLATION_QZSS -> gzssSateliteList.add(status.getCn0DbHz(i))
                GnssStatus.CONSTELLATION_UNKNOWN -> unknownSateliteList.add(status.getCn0DbHz(i))

            }
        }

        if (gpsSateliteList.size == 0) {
            inOut_strength = "실네"
            inOut_nonZero = "실내"
        } else {
            inOut_strength = if (strength_total / size_total < 11F) {
                "실내"
            } else {
                "실외"
            }

            inOut_nonZero = if (size_nonZero < 10) {
                "실내"
            } else {
                "실외"
            }
        }

        updateView()
    }
}