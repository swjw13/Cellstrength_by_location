package com.example.getcellinfos.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.ServiceState
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.getcellinfos.R
import com.example.getcellinfos.listener.LocationManagerAdvanced
import com.example.getcellinfos.listener.phoneStateListener
import com.example.getcellinfos.threadActivity.timerTask
import java.util.*


class MainActivity : AppCompatActivity() {

    private val cellStrengthTextView by lazy {
        findViewById<TextView>(R.id.cellSignalStrengthInfo)
    }
    private val cellLocationTextView by lazy {
        findViewById<TextView>(R.id.cellLocationInfo)
    }
    private val startUpdateButton by lazy {
        findViewById<Button>(R.id.startUpdateButton)
    }
    private val endUpdateButton by lazy {
        findViewById<Button>(R.id.endUpdateButton)
    }
    private val gpsLocationTextview: TextView by lazy {
        findViewById(R.id.cellGPSlocationInfoTextview)
    }
    private val showPhoneStateButton: Button by lazy {
        findViewById(R.id.showPhoneAllStateButton)
    }

    private var locationManager: LocationManager? = null
    private var subscriptionManager: SubscriptionManager? = null
    private var telephonyManager: TelephonyManager? = null
    private var tm1: TelephonyManager? = null

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    private lateinit var listener: phoneStateListener
    private lateinit var listener2: phoneStateListener
    private lateinit var listener3: LocationManagerAdvanced
    private lateinit var listener4: phoneStateListener

    private var isPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initForActivity()
    }

    override fun onPause() {
        super.onPause()

        stopListening()
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
        if (isPermissionGranted) {
            checkPhoneStateIfAvailable()
        } else {
            Toast.makeText(this, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()

        if(isPermissionGranted) {
            startGettingInformation()
        }
    }

    private fun checkPhoneStateIfAvailable() {
        checkisUsimAble()
        checkLocationAble()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            checkPhoneServiceState()
        }
    }

    private fun checkLocationAble() {
        val locState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            locationManager?.isLocationEnabled
        } else {
            true
        }
        if (locState != true) {
            buildDialog("위치를 가져올 수 없습니다.")
        }
    }

    private fun checkisUsimAble() {
        val simState = telephonyManager?.simState
        if (simState != TelephonyManager.SIM_STATE_READY) {
            buildDialog("유심 상태를 학인해 주세요")
        }
        if (telephonyManager?.isNetworkRoaming == true) {
            buildDialog("로밍상태입니다.")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    private fun checkPhoneServiceState() {
        val serviceState = telephonyManager?.serviceState
        if (serviceState?.state != ServiceState.STATE_IN_SERVICE) {
            buildDialog("서비스가 불가능한 상태입니다.")
        }
    }

    private fun buildDialog(text: String) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("경고").setMessage(text)
        builder.setPositiveButton(
            "확인"
        ) { _, _ -> finish() }

        builder.create().show()
    }


    private fun initForActivity() {
        initListener()
        initManager()
    }

    private fun initManager() {
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        subscriptionManager =
            getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    }

    private fun initListener() {
        initButtonListener()
        initManagerListener()
    }

    private fun initButtonListener() {
        startUpdateButton.setOnClickListener {
            startGettingInformation()
        }
        endUpdateButton.setOnClickListener {
            stopListening()
        }
        showPhoneStateButton.setOnClickListener {
            val intent = Intent(this, phoneStateAll::class.java)
            startActivity(intent)
        }
    }

    private fun initManagerListener() {
        listener = phoneStateListener(this)
        listener2 = phoneStateListener(this)
        listener3 = LocationManagerAdvanced(gpsLocationTextview)
        listener4 = phoneStateListener(this)
    }


    private fun startGettingInformation() {

        if (timer == null) {
            timer = Timer()
        }

        requestMyLocation()
        startListeningWithSid()
        getRealtimeLTE()
    }

    @SuppressLint("MissingPermission")
    private fun startListeningWithSid() {
        val ids = subscriptionManager?.activeSubscriptionInfoList

        val subId1 = ids?.get(0)?.subscriptionId ?: return

        tm1 = telephonyManager?.createForSubscriptionId(subId1)

        tm1?.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
        tm1?.listen(listener2, PhoneStateListener.LISTEN_CELL_LOCATION)
        tm1?.listen(listener4, PhoneStateListener.LISTEN_SERVICE_STATE)
    }

    private fun stopListening() {
        stopPhoneStateListener()
        stopLocationUpdate()
    }

    private fun stopPhoneStateListener() {
        tm1?.listen(listener, PhoneStateListener.LISTEN_NONE)
        tm1?.listen(listener2, PhoneStateListener.LISTEN_NONE)
        timer?.cancel()
        timer = null
        timerTask = null
    }

    private fun stopLocationUpdate() {
        locationManager?.removeUpdates(listener3)
    }

    private fun checkPermissions() {
        selfPermissionCheck(
            arrayOf(
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), 101
        )
    }

    private fun selfPermissionCheck(permissions: Array<String>, code: Int) {
        if ((ContextCompat.checkSelfPermission(
                this,
                permissions[0]
            ) == PackageManager.PERMISSION_DENIED)
            || (ContextCompat.checkSelfPermission(
                this,
                permissions[1]
            ) == PackageManager.PERMISSION_DENIED)
            || (ContextCompat.checkSelfPermission(
                this,
                permissions[2]
            ) == PackageManager.PERMISSION_DENIED)
        ) {
            requestPermissions(
                permissions, code
            )
        } else {
            isPermissionGranted = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        fun isPermissionChecked() {
            if ((grantResults.isEmpty() ||
                        (grantResults[0] != PackageManager.PERMISSION_GRANTED) ||
                        (grantResults[1] != PackageManager.PERMISSION_GRANTED) || (grantResults[2] != PackageManager.PERMISSION_GRANTED))
            ) {
                Toast.makeText(this, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show()
            } else {
                isPermissionGranted = true
            }
        }

        when (requestCode) {
            101 -> {
                isPermissionChecked()
            }
        }
    }

    private fun getRealtimeLTE() {
        if (timerTask == null) {
            timerTask = timerTask(
                gpsLocationTextview
            )
            timer?.schedule(timerTask, 1000, 1000)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestMyLocation() {
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.0F, listener3)
        locationManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            1000,
            0.0F,
            listener3
        )
    }
}