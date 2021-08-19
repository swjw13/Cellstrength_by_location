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
import com.example.getcellinfos.Pager.fragments.Gnss.GnssCallback
import com.example.getcellinfos.R
@SuppressLint("SetTextI18n")

class GnssActivity(val context: FragmentActivity) : Fragment() {

    private lateinit var locationManager: LocationManager
    private lateinit var gnssCallback: GnssCallback
    private lateinit var locationListener: LocationListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        locationManager = context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        gnssCallback = GnssCallback{updateView()}

        locationListener = object: LocationListener{

            var timeStamp = 0F
            var inOut = ""

            override fun onLocationChanged(location: Location) {
                inOut = if(location.elapsedRealtimeNanos - timeStamp > 2e9){
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

    fun getCallback(): GnssCallback{
        return gnssCallback
    }

    private fun updateView(){
        view?.findViewById<TextView>(R.id.gpsSatelliteCount)?.text = "감지되는 위성 개수: ${gnssCallback.satCount}"
        view?.findViewById<TextView>(R.id.gpsSatelliteSpecific)?.text = "gps: ${gnssCallback.gpsSateliteList}"
        view?.findViewById<TextView>(R.id.glonasSatelliteSpecific)?.text = "glonas: ${gnssCallback.glonassSateliteList}"
        view?.findViewById<TextView>(R.id.beidouSatelliteSpecific)?.text = "beidou: ${gnssCallback.beidouSateliteList}"
        view?.findViewById<TextView>(R.id.galileoSatelliteSpecific)?.text = "galileo: ${gnssCallback.galileoSateliteList}"
        view?.findViewById<TextView>(R.id.irnssSatelliteSpecific)?.text = "irnss: ${gnssCallback.irnssSateliteList}"
        view?.findViewById<TextView>(R.id.sbasSatelliteSpecific)?.text = "sbas: ${gnssCallback.sbasSateliteList}"
        view?.findViewById<TextView>(R.id.unknownSatelliteSpecific)?.text = "unknown: ${gnssCallback.unknownSateliteList}"
        view?.findViewById<TextView>(R.id.gzssSatelliteSpecific)?.text = "gzss: ${gnssCallback.gzssSateliteList}"
        view?.findViewById<TextView>(R.id.strength_answer)?.text = "strength: ${gnssCallback.strength_total / gnssCallback.size_total}, 결과: ${gnssCallback.inOut_strength}"
    }
}