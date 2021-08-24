package com.example.getcellinfos.overallService.CellinfoService

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.telephony.PhoneStateListener
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception

@SuppressLint("MissingPermission")
class CellAll(val context: Context){
    private val telephonyManager =
        context.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
    private val subscriptionManager =
        context.getSystemService(AppCompatActivity.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

    private var telephonyManagerWithSubscriptionId: TelephonyManager? =
        if (subscriptionManager.activeSubscriptionInfoList.isNotEmpty()) {
            subscriptionManager.activeSubscriptionInfoList?.get(0)?.subscriptionId?.let {
                telephonyManager.createForSubscriptionId(it)
            }
        } else {
            telephonyManager
        }

    private var mStrengthListener: StrengthListener? = null
    private var mCellularListener: CellInfoListener? = null

    fun listenForCellUpdate(listener: PhoneStateListener, listeningType: Int) {
        try {
            when (listeningType) {
                PhoneStateListener.LISTEN_SIGNAL_STRENGTHS -> {
                    if (mStrengthListener == null) {
                        mStrengthListener = listener as StrengthListener
                    }
                }
                PhoneStateListener.LISTEN_CELL_INFO -> {
                    if (mCellularListener == null) {
                        mCellularListener = listener as CellInfoListener
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }

        try {
            telephonyManagerWithSubscriptionId?.listen(listener, listeningType)
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun stopListening() {
        telephonyManagerWithSubscriptionId?.listen(
            mStrengthListener,
            PhoneStateListener.LISTEN_NONE
        )
        telephonyManagerWithSubscriptionId?.listen(
            mCellularListener,
            PhoneStateListener.LISTEN_NONE
        )
    }

    fun getCellList(): List<Int> {
        return if ((mStrengthListener != null) && (mCellularListener != null)) {
            mStrengthListener!!.getStrengthList() + mCellularListener!!.getCellList()
        } else {
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
        }
    }
}