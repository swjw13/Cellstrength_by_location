package com.example.getcellinfos.Pager.fragments.FiveG

import android.annotation.SuppressLint
import android.graphics.Color
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
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.getcellinfos.R

@SuppressLint("SetTextI18n")
class FiveGFragment(val context: FragmentActivity) : Fragment() {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var telephonyManager: TelephonyManager
    private lateinit var phoneStateListener: PhoneStateListener
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

        phoneStateListener = object: PhoneStateListener(){
            override fun onSignalStrengthsChanged(signalStrength: SignalStrength?) {
                super.onSignalStrengthsChanged(signalStrength)

                ssRsrp = 0
                ssRsrq = 0
                ssSinr = 0

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    signalStrength?.cellSignalStrengths?.forEach {
                        if(it is CellSignalStrengthNr){
                            ssRsrp = it.ssRsrp
                            ssRsrq = it.ssRsrq
                            ssSinr = it.ssSinr

                            view?.findViewById<TextView>(R.id.nrRsrpTextView)?.text = "ssRsrp: $ssRsrp"
                            view?.findViewById<TextView>(R.id.nrRsrqTextView)?.text = "ssRsrq: $ssRsrq"
                            view?.findViewById<TextView>(R.id.nrRssiTextView)?.text = "ssSinr: $ssSinr"

                            view?.findViewById<CardView>(R.id.nrCardView)?.setCardBackgroundColor(Color.BLUE)
                        }
                    }
                    if(ssRsrp == 0){
                        view?.findViewById<TextView>(R.id.nrRsrpTextView)?.text = "ssRsrp: -"
                        view?.findViewById<TextView>(R.id.nrRsrqTextView)?.text = "ssRsrq: -"
                        view?.findViewById<TextView>(R.id.nrRssiTextView)?.text = "ssSinr: -"

                        view?.findViewById<CardView>(R.id.nrCardView)?.setCardBackgroundColor(Color.RED)
                    }
                }
            }
        }

        return inflater.inflate(R.layout.activity_five_gactivity, container, false)
    }

    override fun onResume() {
        super.onResume()

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
    }

    override fun onDestroy() {
        super.onDestroy()
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
    }
}