package com.example.getcellinfos.Pager.gnssActivity

import android.annotation.SuppressLint
import android.location.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.getcellinfos.R
import org.w3c.dom.Text

class GnssActivity(val context: FragmentActivity) : Fragment() {

    private lateinit var locationManager: LocationManager

    var gpsSateliteList: MutableList<Float> = mutableListOf()
    private var sbasSateliteList: MutableList<Float> = mutableListOf()
    private var glonasSateliteList: MutableList<Float> = mutableListOf()
    private var elseSateliteList: MutableList<Float> = mutableListOf()

    private lateinit var gnssCallback: GnssStatus.Callback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        locationManager = context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        gnssCallback = object: GnssStatus.Callback(){
            @SuppressLint("SetTextI18n")
            override fun onSatelliteStatusChanged(status: GnssStatus) {
                gpsSateliteList = mutableListOf()
                sbasSateliteList = mutableListOf()
                glonasSateliteList = mutableListOf()
                elseSateliteList = mutableListOf()

                for (i in 0 until status.satelliteCount - 1) {
                    when (status.getConstellationType(i)) {
                        GnssStatus.CONSTELLATION_GPS -> gpsSateliteList!!.add(status.getCn0DbHz(i))
                        GnssStatus.CONSTELLATION_SBAS -> sbasSateliteList.add(status.getCn0DbHz(i))
                        GnssStatus.CONSTELLATION_GLONASS -> glonasSateliteList.add(status.getCn0DbHz(i))
                        else -> elseSateliteList.add(status.getCn0DbHz(i))
                    }
                }

                super.onSatelliteStatusChanged(status)
                view?.findViewById<TextView>(R.id.first)?.text = "위성 갯수: ${status.satelliteCount}"
                view?.findViewById<TextView>(R.id.second)?.text = "gps: $gpsSateliteList"
                view?.findViewById<TextView>(R.id.third)?.text = "SBAS: $sbasSateliteList"
                view?.findViewById<TextView>(R.id.fourth)?.text = "glonas: $glonasSateliteList"
                view?.findViewById<TextView>(R.id.fifth)?.text = "else: $elseSateliteList"
            }
        }
        return inflater.inflate(R.layout.activity_gnss, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()

        locationManager.registerGnssStatusCallback(gnssCallback, Handler(context.mainLooper))
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0.0F
        ) { }
    }

    override fun onStop() {
        super.onStop()
        locationManager.unregisterGnssStatusCallback(gnssCallback)
    }
}