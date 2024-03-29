package com.example.getcellinfos

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.telephony.*
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.getcellinfos.Pager.PagerMainActivity.PagerActivity
import com.example.getcellinfos.activities.SettingActivity
import com.example.getcellinfos.appDatabase.Instance.DatabaseBuilder
import com.example.getcellinfos.appDatabase.Instance.DatabaseManager
import com.example.getcellinfos.appDatabase.logs.CellInfo
import com.example.getcellinfos.listener.LocationManagerAdvanced
import com.example.getcellinfos.otherCellList.OtherCellListViewAdapter
import com.example.getcellinfos.overallService.CellInfoListener
import com.example.getcellinfos.overallService.OverAllClass
import com.example.getcellinfos.overallService.StrengthListener
import com.example.getcellinfos.threadActivity.timerTask
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import java.util.*

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {

    private val updateDBButton: FloatingActionButton by lazy {
        findViewById(R.id.addDbButton)
    }
    private val addMemoButton: FloatingActionButton by lazy {
        findViewById(R.id.addMemoButton)
    }
    private val wifiInfoButton: FloatingActionButton by lazy {
        findViewById(R.id.wifiInfoButton)
    }
    private val otherCellsRecyclerView: RecyclerView by lazy {
        findViewById(R.id.otherCellsRecyclerView)
    }

    private lateinit var mapFragment: MapFragment
    private lateinit var naverMap: NaverMap
    private lateinit var mLocationSource: FusedLocationSource
    private var marker: Marker? = null

    private var locationManager: LocationManager? = null
    private var telephonyManager: TelephonyManager? = null

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    private lateinit var listenerForLatitude: LocationManagerAdvanced
    private lateinit var listenerForSignalStrength: StrengthListener
    private lateinit var listenerForCellInfos: CellInfoListener

    private var isPermissionGranted = false
    private var settingNumber = 1
    private var Memos = ""

    private lateinit var databaseManager: DatabaseManager

    private lateinit var adapter: OtherCellListViewAdapter

    private lateinit var overallClass: OverAllClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initForActivity()
        overallClass = OverAllClass(this)
    }

    private fun initForActivity() {
        initForView()
        initForService()
    }

    private fun initForView() {
        initMap()
        initButtonListener()
        initRecyclerView()
    }

    private fun initForService() {
        initDatabase()
        initManager()
    }

    private fun initRecyclerView() {
        adapter = OtherCellListViewAdapter()
        otherCellsRecyclerView.layoutManager = LinearLayoutManager(this)
        otherCellsRecyclerView.adapter = adapter
    }

    private fun initDatabase() {
        databaseManager = DatabaseBuilder(this).getInstance("log")!!
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
        wifiInfoButton.setOnClickListener {
            val intent = Intent(this, PagerActivity::class.java)
            startActivity(intent)
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
        databaseManager.exportCSV(this)
    }

    private fun initManager() {
        initTelephoneManager()
        initLocationManager()
    }

    @SuppressLint("MissingPermission", "NotifyDataSetChanged")
    private fun initTelephoneManager() {
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        listenerForSignalStrength = StrengthListener(update = { list ->
            updateStrengthView(list)
        })
        listenerForCellInfos = CellInfoListener(updateView = { list -> updateCellInfo(list) },
            updateAdapter = { list ->
                adapter.list = list
                adapter.notifyDataSetChanged()
            },
            updateMap = { lat, lng ->
                updateMap(lat, lng)
            })
    }

    private fun updateCellInfo(cellInfoList: MutableList<Int>) {
        findViewById<TextView>(R.id.neighborcellTextView).text = cellInfoList[2].toString()
        findViewById<TextView>(R.id.earfcnTextView).text = "earfcn: ${cellInfoList[0]}"
        findViewById<TextView>(R.id.pciTextView).text = "pci: ${cellInfoList[1]}"
    }

    private fun updateStrengthView(strengthList: List<Int>) {
        findViewById<TextView>(R.id.rsrpTextView).text = "rsrp: ${strengthList[0]}"
        findViewById<TextView>(R.id.rsrqTextView).text = "rsrq: ${strengthList[1]}"
        findViewById<TextView>(R.id.rssiTextView).text = "rssi: ${strengthList[2]}"
        findViewById<TextView>(R.id.rssnrTextView).text = "rssnr: ${strengthList[3]}"
    }

    private fun updateMap(lat: Float, lon: Float) {
        if (marker != null) {
            marker!!.map = null
            marker!!.position = LatLng(lat.toDouble(), lon.toDouble())
        } else {
            marker = Marker()
            marker!!.position = LatLng(lat.toDouble(), lon.toDouble())
        }
        marker!!.map = naverMap
        marker?.setOnClickListener {
            Toast.makeText(this, marker?.position.toString(), Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun initLocationManager() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        listenerForLatitude = LocationManagerAdvanced(update = { lat, lon, alt ->
            findViewById<TextView>(R.id.cellLocationTextView).text =
                "%.5f".format(lat) + "/" + "%.5f".format(lon) + "/" + "%.5f".format(alt)
        }, changeMap = { latitude, longitude ->
            moveMap(latitude, longitude)
        })
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
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.ACTIVITY_RECOGNITION
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
                            Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    isPermissionGranted = true
                    checkPhoneStateIfAvailable()
                } else {
                    Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
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
        settingNumber = acquireSettings()

        if (isPermissionGranted) {
            when (settingNumber) {
                1 -> {
                    startGettingInformation()
                }
                2 -> {
                    startGettingInformationWithTimertask()
                }
                else -> {
                    startGettingInformation()
                }
            }
        }
    }

    private fun startGettingInformationWithTimertask() {
        startGettingInformation()
        addDatabaseTimerTask(intent?.getIntExtra("autoTime", 0) ?: 1)
    }

    private fun acquireSettings(): Int {
        return intent?.getIntExtra("settingNumber", 1) ?: 1
    }

    private fun moveMap(latitude: Double, longitude: Double) {
        naverMap.cameraPosition = CameraPosition(LatLng(latitude, longitude), 16.0)
    }

    private fun startGettingInformation() {
        requestMyLocation()
        startListeningWithSid()
    }

    @SuppressLint("MissingPermission")
    private fun requestMyLocation() {
        overallClass.locationService()
            .listenForLocationUpdate(LocationManager.NETWORK_PROVIDER, listenerForLatitude)
    }

    private fun startListeningWithSid() {
        overallClass.cellService().listenForCellUpdate(
            listenerForSignalStrength,
            PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
        )
        overallClass.cellService()
            .listenForCellUpdate(listenerForCellInfos, PhoneStateListener.LISTEN_CELL_INFO)
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
        overallClass.cellService().stopListening()
        timer?.cancel()
        timer = null
        timerTask = null
    }

    private fun stopLocationUpdate() {
        overallClass.locationService().stopLocationUpdate()
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
        val others_rsrp = mutableListOf<Int>()
        val others_rsrq = mutableListOf<Int>()
        val others_rssi = mutableListOf<Int>()
        val others_rssnr = mutableListOf<Int>()
        val others_earfcn = mutableListOf<Int>()
        val others_pci = mutableListOf<Int>()
        for (i in listenerForCellInfos.otherCellList) {
            others_rsrp.add(i.rsrp)
            others_rsrq.add(i.rsrq)
            others_rssi.add(i.rssi)
            others_rssnr.add(i.rssnr)
            others_earfcn.add(i.earfcn)
            others_pci.add(i.pci)
        }
        databaseManager.insert(
            CellInfo(
                uid = null,
                date = overallClass.getCurrentTimeFromFormat("yyyy-MM-dd"),
                time = overallClass.getCurrentTimeFromFormat("hh:mm:ss"),
                latitude = overallClass.locationService().getLocation()[0].toString(),
                longitude = overallClass.locationService().getLocation()[1].toString(),
                altitude = overallClass.locationService().getLocation()[2].toString(),
                rsrp = overallClass.cellService().getCellList()[0],
                rsrq = overallClass.cellService().getCellList()[1],
                rssi = overallClass.cellService().getCellList()[2],
                rssnr = overallClass.cellService().getCellList()[3],
                earfcn = overallClass.cellService().getCellList()[4],
                pci = overallClass.cellService().getCellList()[5],
                neighborCell = overallClass.cellService().getCellList()[6],
                memo = Memos,
                other_cell_rsrp = others_rsrp.toString(),
                other_cell_rsrq = others_rsrq.toString(),
                other_cell_rssi = others_rssi.toString(),
                other_cell_rssnr = others_rssnr.toString(),
                other_cell_earfcn = others_earfcn.toString(),
                other_cell_Pci = others_pci.toString()
            )
        )
        Memos = ""
        runOnUiThread {
            Toast.makeText(this, "로그 등록 완료", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addDatabaseTimerTask(autotime: Int) {
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
        databaseManager.deleteAll()
        Toast.makeText(this, "로그 삭제 완료", Toast.LENGTH_SHORT).show()
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
}