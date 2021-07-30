package com.example.getcellinfos

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.telephony.PhoneStateListener
import android.telephony.ServiceState
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.ajts.androidmads.library.SQLiteToExcel
import com.example.getcellinfos.Activities.SettingActivity
import com.example.getcellinfos.WifiClass.WifiListener
import com.example.getcellinfos.WifiClass.WifiScanListener
import com.example.getcellinfos.WifiClass.WifiScanReceiver
import com.example.getcellinfos.appDatabase.AppDatabase
import com.example.getcellinfos.appDatabase.CSVExportListener
import com.example.getcellinfos.dataClass.CellInfo
import com.example.getcellinfos.listener.LocationManagerAdvanced
import com.example.getcellinfos.listener.phoneStateListener
import com.example.getcellinfos.threadActivity.timerTask
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val updateDBButton: FloatingActionButton by lazy {
        findViewById(R.id.addDbButton)
    }
    private val addMemoButton: FloatingActionButton by lazy {
        findViewById(R.id.addMemoButton)
    }
    private val locationTextView: TextView by lazy {
        findViewById(R.id.cellLocationTextView)
    }
    private val neighborCellTextView: TextView by lazy {
        findViewById(R.id.neighborcellTextView)
    }


    private lateinit var mapFragment: SupportMapFragment
    private lateinit var database: AppDatabase

    private var wifiManager: WifiManager? = null
    private var locationManager: LocationManager? = null
    private var subscriptionManager: SubscriptionManager? = null
    private var telephonyManager: TelephonyManager? = null
    private var tm1: TelephonyManager? = null
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    private lateinit var listenerForSignalStrength: phoneStateListener
    private lateinit var listener2: phoneStateListener
    private lateinit var listener3: LocationManagerAdvanced
    private lateinit var listenerForCellInfos: phoneStateListener

    private lateinit var wifiListener: WifiListener
    private lateinit var wifiScanReceiver: BroadcastReceiver

    private var isPermissionGranted = false
    private var settingNumber = 2
    private var Memos = ""
    private var neighborCell: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initForActivity()
    }

    private fun initForActivity() {
        initDatabase()
        initMap()
        initButtonListener()
        initManager()
    }
    private fun initDatabase(){
        database = Room.databaseBuilder(this, AppDatabase::class.java, "CellInfo").build()
    }
    @SuppressLint("MissingPermission")
    private fun initMap(){
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    }
    private fun initButtonListener() {
        addMemoButton.setOnClickListener {
            makeMemoDialog()
        }
        updateDBButton.setOnClickListener {
            addDataToDB()
        }
    }
    private fun makeMemoDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.memo_dialog)
        dialog.findViewById<EditText>(R.id.memoEditText).text.append(Memos)

        dialog.findViewById<Button>(R.id.memoSubmitButton).setOnClickListener {
            val memos = dialog.findViewById<EditText>(R.id.memoEditText).text.toString()
            Memos = memos
            Toast.makeText(this, "매모 작성 완료", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun exportCSV() {
        val currentDir = Environment.getExternalStorageDirectory().toString() + File.separator
        val dir = currentDir + "CellInfo/CSV/"
        if(!File(dir).exists()) {
            File(dir).mkdirs()
        }
        val time = System.currentTimeMillis()

        val sqlToExcel = SQLiteToExcel(this, "CellInfo", dir)
        sqlToExcel.exportAllTables("$time.csv", CSVExportListener(this))
    }

    private fun initManager() {
        initTelephoneManager()
        initLocationManager()
        initWifiManager()
    }

    private fun initTelephoneManager() {
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        subscriptionManager =
            getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        listenerForSignalStrength = phoneStateListener(this)
//        listener2 = phoneStateListener(this)
        listenerForCellInfos = phoneStateListener(this)
    }

    private fun initLocationManager() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        listener3 = LocationManagerAdvanced(locationTextView) { latitude, longitude ->
            moveMap(latitude, longitude)
        }
    }

    private fun initWifiManager() {
        wifiManager = getSystemService(WIFI_SERVICE) as WifiManager
        wifiListener = WifiScanListener(this)
        wifiScanReceiver = WifiScanReceiver(wifiListener, wifiManager!!)
    }


    override fun onStart() {
        super.onStart()
        isPermissionGranted = false

        checkPermissions()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivity(intent)
            }
        }
        selfPermissionCheck(
            arrayOf(
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
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
            || (ContextCompat.checkSelfPermission(
                this,
                permissions[3]
            ) == PackageManager.PERMISSION_DENIED)
            || (ContextCompat.checkSelfPermission(
                this,
                permissions[4]
            ) == PackageManager.PERMISSION_DENIED)
        ) {
            requestPermissions(
                permissions, code
            )
        } else {
            isPermissionGranted = true
            checkPhoneStateIfAvailable()

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
                        (grantResults[1] != PackageManager.PERMISSION_GRANTED) ||
                        (grantResults[2] != PackageManager.PERMISSION_GRANTED) ||
                        (grantResults[3] != PackageManager.PERMISSION_GRANTED) ||
                        (grantResults[4] != PackageManager.PERMISSION_GRANTED))
            ) {
                Toast.makeText(this, "권한이 거부되었습니다 " + permissions[4], Toast.LENGTH_SHORT).show()
                finish()
            } else {
                isPermissionGranted = true
                checkPhoneStateIfAvailable()
            }
        }

        isPermissionChecked()
    }

    private fun checkPhoneStateIfAvailable() {
        checkisUsimAble()
        checkLocationAble()
        checkPhoneServiceState()
    }

    private fun checkLocationAble() {
        val locState =
            locationManager?.isLocationEnabled
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

    @SuppressLint("MissingPermission")
    private fun checkPhoneServiceState() {
        val serviceState = telephonyManager?.serviceState
        if (serviceState?.state != ServiceState.STATE_IN_SERVICE) {
            buildDialog("서비스가 불가능한 상태입니다.")
        }
    }





    override fun onResume() {
        super.onResume()

        val settings = intent?.getIntExtra("settingNumber", 1)
        settingNumber = settings ?: 1
        val autotime = intent?.getIntExtra("autoTime", 0)

        // TODO: 기지국 정보를 받아와서 저장하기 (이후 맵에 추가)

        if (isPermissionGranted) {
            when (settingNumber) {
                1 -> {startGettingInformation()}
                2 -> {
                    startGettingInformation()
                    addTimerTask(autotime)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun moveMap(latitude: Double, longitude: Double) {
        val latLng = LatLng(latitude, longitude)
        mapFragment.getMapAsync {
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.toFloat()))
            it.isMyLocationEnabled = true
        }

        neighborCell = tm1?.allCellInfo?.size
        neighborCellTextView.text = neighborCell.toString()
    }

    private fun startGettingInformation() {
        requestMyLocation()
        startListeningWithSid()
//        startCheckingWifi()
    }

    @SuppressLint("MissingPermission")
    private fun requestMyLocation() {
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0F, listener3)
        locationManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0,
            0.0F,
            listener3
        )
    }

    @SuppressLint("MissingPermission")
    private fun startListeningWithSid() {
        val ids = subscriptionManager?.activeSubscriptionInfoList

        val subId1 = ids?.get(0)?.subscriptionId ?: return

        tm1 = telephonyManager?.createForSubscriptionId(subId1)

        tm1?.listen(listenerForSignalStrength, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
//        tm1?.listen(listener2, PhoneStateListener.LISTEN_CELL_LOCATION)
        tm1?.listen(listenerForCellInfos, PhoneStateListener.LISTEN_CELL_INFO)
    }

    private fun startCheckingWifi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            wifiManager?.registerScanResultsCallback(mainExecutor,
                object : WifiManager.ScanResultsCallback() {
                    override fun onScanResultsAvailable() {
                        Log.d("테스트", "Wifi change detected@@@@@")
                    }
                })
        } else {
            setupWifiChecking()
            wifiManager?.startScan()
        }
    }

    private fun setupWifiChecking() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        applicationContext.registerReceiver(wifiScanReceiver, intentFilter)
    }





    override fun onPause() {
        super.onPause()

        stopListening()
    }

    private fun stopListening() {
        stopPhoneStateListener()
        stopLocationUpdate()
    }

    private fun stopPhoneStateListener() {
        tm1?.listen(listenerForSignalStrength, PhoneStateListener.LISTEN_NONE)
        tm1?.listen(listenerForCellInfos, PhoneStateListener.LISTEN_NONE)
        timer?.cancel()
        timer = null
        timerTask = null
    }

    private fun stopLocationUpdate() {
        locationManager?.removeUpdates(listener3)
    }

    private fun buildDialog(text: String) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("경고").setMessage(text)
        builder.setPositiveButton(
            "확인"
        ) { _, _ -> finish() }

        builder.create().show()
    }


    private fun addDataToDB() {

        val currentDate = getDateFormat("yyyy-MM-dd")
        val currentTime = getDateFormat("hh:mm:ss")

        Thread {
            database.cellInfoDto().insertLog(
                CellInfo(
                    uid = null,
                    date = currentDate,
                    time = currentTime,
                    latitude = listener3.latitude,
                    longitude = listener3.longitude,
                    altitude = listener3.altitude,
                    rsrp = listenerForCellInfos.list[0],
                    rsrq = listenerForCellInfos.list[1],
                    rssi = listenerForCellInfos.list[2],
                    rssnr = listenerForCellInfos.list[3],
                    earfcn = listenerForCellInfos.list[4],
                    pci = listenerForCellInfos.list[5],
                    neighborCell = neighborCell ?: 0,
                    memo = Memos
                )
            )
            Memos = ""
            runOnUiThread {
                Toast.makeText(this, "로그 등록 완료", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }


    private fun addTimerTask(autotime: Int?) {

        if (timer == null) {
            timer = Timer()
        }

        if (timerTask == null) {
            timerTask = timerTask {
                addDataToDB()
            }
        }
        timer?.schedule(timerTask, 1000 * (autotime ?: 1).toLong(), 1000 * (autotime ?: 1).toLong())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.optionMenu -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.exportCsv -> {
                exportCSV()
            }
            R.id.deleteDB -> {
                deleteDBTable()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun deleteDBTable(){
        Thread{
            database.cellInfoDto().clearTable()
            runOnUiThread {
                Toast.makeText(this, "삭제 완료", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateFormat(pattern: String): String{
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat(pattern)
        return dateFormat.format(date)
    }
}