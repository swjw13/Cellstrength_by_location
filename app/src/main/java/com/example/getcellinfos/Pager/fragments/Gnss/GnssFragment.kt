package com.example.getcellinfos.Pager.Fragments.gnssActivity

import android.annotation.SuppressLint
import android.location.*
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.getcellinfos.R
@SuppressLint("SetTextI18n")

class GnssActivity(val context: FragmentActivity) : Fragment() {

    private lateinit var locationManager: LocationManager

    var gpsSateliteList: MutableList<Float> = mutableListOf()
    private var sbasSateliteList: MutableList<Float> = mutableListOf()
    private var glonasSateliteList: MutableList<Float> = mutableListOf()
    private var elseSateliteList: MutableList<Float> = mutableListOf()

    private lateinit var gnssCallback: GnssStatus.Callback
    private lateinit var locationListener: LocationListener
    private var strength_total = 0F
    private var size_total = 0
    private var size_nonZero = 0
    private var inOut_strength = ""
    private var inOut_nonZero = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        locationManager =
            context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

        gnssCallback = object : GnssStatus.Callback() {
            override fun onSatelliteStatusChanged(status: GnssStatus) {
                super.onSatelliteStatusChanged(status)

                gpsSateliteList = mutableListOf()
                sbasSateliteList = mutableListOf()
                glonasSateliteList = mutableListOf()
                elseSateliteList = mutableListOf()

                strength_total = 0F
                size_total = 0
                size_nonZero = 0

                for (i in 0 until status.satelliteCount - 1) {
                    when (status.getConstellationType(i)) {
                        GnssStatus.CONSTELLATION_GPS -> {
                            gpsSateliteList.add(status.getCn0DbHz(i))

                            if (status.getCn0DbHz(i) != 0F) {
                                strength_total += status.getCn0DbHz(i)
                                size_nonZero += 1
                            }
                            size_total += 1
                        }

                        GnssStatus.CONSTELLATION_SBAS -> sbasSateliteList.add(status.getCn0DbHz(i))

                        GnssStatus.CONSTELLATION_GLONASS -> glonasSateliteList.add(status.getCn0DbHz(i))

                        else -> elseSateliteList.add(status.getCn0DbHz(i))
                    }
                }

                inOut_strength = if (strength_total / size_total < 13.5F) {
                    "실내"
                } else {
                    "실외"
                }

                inOut_nonZero = if (size_nonZero < 10) {
                    "실내"
                } else {
                    "실외"
                }

                view?.findViewById<TextView>(R.id.first)?.text = "위성 갯수: ${status.satelliteCount}"
                view?.findViewById<TextView>(R.id.second)?.text = "gps: $gpsSateliteList"
                view?.findViewById<TextView>(R.id.strength_answer)?.text =
                    "strength: ${strength_total / size_total}, 결과: $inOut_strength"
            }
        }

        locationListener = object: LocationListener{

            var timeStamp = 0F
            var inOut = ""

            override fun onLocationChanged(location: Location) {
                inOut = if(location.elapsedRealtimeNanos - timeStamp > 1000F){
                    "실내"
                } else{
                    "실외"
                }
                view?.findViewById<TextView>(R.id.number_answer)?.text = "time: ${location.elapsedRealtimeNanos - timeStamp}, $inOut"
                timeStamp = location.elapsedRealtimeNanos.toFloat()
            }
        }

        return inflater.inflate(R.layout.activity_gnss, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        locationManager.registerGnssStatusCallback(gnssCallback, Handler(context.mainLooper))
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0.0F,
            locationListener
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.unregisterGnssStatusCallback(gnssCallback)
    }
}