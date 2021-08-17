package com.example.getcellinfos.Pager.fragments.FiveG

import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.telephony.CellSignalStrengthNr
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.getcellinfos.R

class FiveGFragment(val context: FragmentActivity) : Fragment() {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var telephonyManager: TelephonyManager
    var ssRsrp = 0
    var ssRsrq = 0
    var ssSinr = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        connectivityManager = context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        telephonyManager = context.getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager

        return inflater.inflate(R.layout.activity_five_gactivity, container, false)
    }

    override fun onResume() {
        super.onResume()

        Log.d("jae","network id: ${connectivityManager.activeNetwork}")

        telephonyManager.listen(object: PhoneStateListener(){
            override fun onSignalStrengthsChanged(signalStrength: SignalStrength?) {
                super.onSignalStrengthsChanged(signalStrength)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    signalStrength?.cellSignalStrengths?.forEach {
                        if(it is CellSignalStrengthNr){
                            ssRsrp = it.ssRsrp
                            ssRsrq = it.ssRsrq
                            ssSinr = it.ssSinr
                            view?.findViewById<TextView>(R.id.nrRsrpTextView)?.text = "ssRsrp: $ssRsrp"
                            view?.findViewById<TextView>(R.id.nrRsrqTextView)?.text = "ssRsrq: $ssRsrq"
                            view?.findViewById<TextView>(R.id.nrRssiTextView)?.text = "ssSinr: $ssSinr"
                        }
                    }
                }
            }
        }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)

    }
}