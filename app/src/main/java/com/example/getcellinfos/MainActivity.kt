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
import android.telephony.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
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
    private val mainScrollView: ScrollView by lazy {
        findViewById(R.id.scrollMain)
    }

    private lateinit var mapFragment: MapFragment
    private lateinit var database: AppDatabase
    private lateinit var naverMap: NaverMap
    private lateinit var mLocationSource: FusedLocationSource

    private var wifiManager: WifiManager? = null
    private var locationManager: LocationManager? = null
    private var subscriptionManager: SubscriptionManager? = null
    private var telephonyManager: TelephonyManager? = null
    private var tm1: TelephonyManager? = null
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    private lateinit var listenerForSignalStrength: phoneStateListener
    private lateinit var listenerForLatitude: LocationManagerAdvanced
    private lateinit var listenerForCellInfos: phoneStateListener

    private lateinit var wifiListener: WifiListener
    private lateinit var wifiScanReceiver: BroadcastReceiver

    private var isPermissionGranted = false
    private var settingNumber = 2
    private var Memos = ""
    private var neighborCell: Int? = 0

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initForActivity()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun initForActivity() {
        initDatabase()
        initMap()
        initButtonListener()
        initManager()
    }

    private fun initDatabase() {
        database = Room.databaseBuilder(this, AppDatabase::class.java, "CellInfo").build()
    }

    @SuppressLint("MissingPermission")
    private fun initMap() {
        val fm = supportFragmentManager
        mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync { nMap ->
            naverMap = nMap
            mLocationSource = FusedLocationSource(this, 102)
            naverMap.locationSource = mLocationSource
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        }
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
            Memos = dialog.findViewById<EditText>(R.id.memoEditText).text.toString()
            Toast.makeText(this, "매모 작성 완료", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun exportCSV() {
        val dir =
            Environment.getExternalStorageDirectory().toString() + File.separator + "CellInfo/CSV/"
        if (!File(dir).exists()) {
            File(dir).mkdirs()
        }
        val time = System.currentTimeMillis()

        val sqlToExcel = SQLiteToExcel(this, "CellInfo", dir)
        sqlToExcel.exportAllTables("$time.csv", CSVExportListener { text ->
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        })
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun initManager() {
        initTelephoneManager()
        initLocationManager()
//        initWifiManager()
    }

    private fun initTelephoneManager() {
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        subscriptionManager =
            getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        listenerForSignalStrength = phoneStateListener(mainScrollView)
        listenerForCellInfos = phoneStateListener(mainScrollView)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun initLocationManager() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        listenerForLatitude = LocationManagerAdvanced(locationTextView) { latitude, longitude ->
            moveMap(latitude, longitude)
        }
    }

//    private fun initWifiManager() {
//        wifiManager = getSystemService(WIFI_SERVICE) as WifiManager
//        wifiListener = WifiScanListener(this)
//        wifiScanReceiver = WifiScanReceiver(wifiListener, wifiManager!!)
//    }


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
        var permissionCheck = true
        permissions.forEach { permission ->
            if ((ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_DENIED)
            ) {
                permissionCheck = false
            }
        }
        if (permissionCheck) {
            isPermissionGranted = true
            checkPhoneStateIfAvailable()
        } else {
            requestPermissions(permissions, code)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            101 -> {
                if (grantResults.isNotEmpty()) {
                    grantResults.forEach { permission ->
                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "권한이 거부되었습니다.1", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    isPermissionGranted = true
                    checkPhoneStateIfAvailable()
                } else {
                    Toast.makeText(this, "권한이 거부되었습니다.2", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            102 -> {

            }
        }

    }

    private fun checkPhoneStateIfAvailable() {
        checkisUsimAble()
        checkLocationAble()
        checkPhoneServiceState()
    }

    private fun checkLocationAble() {
        if (locationManager?.isLocationEnabled != true) {
            buildDialog("위치를 가져올 수 없습니다.")
        }
    }

    private fun checkisUsimAble() {
        if (telephonyManager?.simState != TelephonyManager.SIM_STATE_READY) {
            buildDialog("유심 상태를 학인해 주세요")
        }
        if (telephonyManager?.isNetworkRoaming == true) {
            buildDialog("로밍상태입니다.")
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkPhoneServiceState() {
        if (telephonyManager?.serviceState?.state != ServiceState.STATE_IN_SERVICE) {
            buildDialog("서비스가 불가능한 상태입니다.")
        }
    }


    override fun onResume() {
        super.onResume()

        startGettingInfo()
    }

    private fun startGettingInfo() {
        acquireSettings()

        // TODO: 기지국 정보를 받아와서 저장하기 (이후 맵에 추가)

        if (isPermissionGranted) {
            when (settingNumber) {
                1 -> {
                    startGettingInformation()
                }
                2 -> {
                    startGettingInformationWithTimertask()
                }
            }
        }
    }

    private fun startGettingInformationWithTimertask() {
        startGettingInformation()
        addTimerTask(intent?.getIntExtra("autoTime", 0) ?: 1)
    }

    private fun acquireSettings() {
        settingNumber = intent?.getIntExtra("settingNumber", 1) ?: 1
    }

    @SuppressLint("MissingPermission")
    fun moveMap(latitude: Double, longitude: Double) {
        naverMap.cameraPosition = CameraPosition(LatLng(latitude, longitude), 17.0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tm1?.requestCellInfoUpdate(mainExecutor,
                object : TelephonyManager.CellInfoCallback() {
                    override fun onCellInfo(cellInfo: MutableList<android.telephony.CellInfo>) {
                        neighborCell = cellInfo.size
                        neighborCellTextView.text = neighborCell.toString()
                    }
                })
        } else {
            neighborCell = tm1?.allCellInfo?.size
            neighborCellTextView.text = neighborCell.toString()
        }
    }

    private fun startGettingInformation() {
        requestMyLocation()
        startListeningWithSid()
//        startCheckingWifi()
    }

    @SuppressLint("MissingPermission")
    private fun requestMyLocation() {
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0.0F,
            listenerForLatitude
        )
        locationManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0,
            0.0F,
            listenerForLatitude
        )
    }

    @SuppressLint("MissingPermission")
    private fun startListeningWithSid() {
        val subId1 =
            subscriptionManager?.activeSubscriptionInfoList?.get(0)?.subscriptionId ?: return

        tm1 = telephonyManager?.createForSubscriptionId(subId1)

        tm1?.listen(listenerForSignalStrength, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
        tm1?.listen(listenerForCellInfos, PhoneStateListener.LISTEN_CELL_INFO)
    }


//    private fun startCheckingWifi() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            wifiManager?.registerScanResultsCallback(mainExecutor,
//                object : WifiManager.ScanResultsCallback() {
//                    override fun onScanResultsAvailable() {
//                        Log.d("테스트", "Wifi change detected@@@@@")
//                    }
//                })
//        } else {
//            setupWifiChecking()
//            wifiManager?.startScan()
//        }
//    }
//
//    private fun setupWifiChecking() {
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
//        applicationContext.registerReceiver(wifiScanReceiver, intentFilter)
//    }


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
        locationManager?.removeUpdates(listenerForLatitude)
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
        Thread {
            database.cellInfoDto().insertLog(
                CellInfo(
                    uid = null,
                    date = getDateFormat("yyyy-MM-dd"),
                    time = getDateFormat("hh:mm:ss"),
                    latitude = listenerForLatitude.latitude.toString(),
                    longitude = listenerForLatitude.longitude.toString(),
                    altitude = listenerForLatitude.altitude.toString(),
                    rsrp = listenerForSignalStrength.list[0],
                    rsrq = listenerForSignalStrength.list[1],
                    rssi = listenerForSignalStrength.list[2],
                    rssnr = listenerForSignalStrength.list[3],
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


    private fun addTimerTask(autotime: Int) {
        if (timer == null) {
            timer = Timer()
        }

        if (timerTask == null) {
            timerTask = timerTask {
                addDataToDB()
            }
        }
        timer?.schedule(timerTask, 1000 * autotime.toLong(), 1000 * autotime.toLong())
    }

    private fun deleteDBTable() {
        Thread {
            database.cellInfoDto().clearTable()
            runOnUiThread {
                Toast.makeText(this, "삭제 완료", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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

    @SuppressLint("SimpleDateFormat")
    private fun getDateFormat(pattern: String): String {
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat(pattern)
        return dateFormat.format(date)
    }
}