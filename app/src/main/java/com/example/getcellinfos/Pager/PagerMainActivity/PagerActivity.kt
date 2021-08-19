package com.example.getcellinfos.Pager.PagerMainActivity

import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.getcellinfos.R
import com.example.getcellinfos.appDatabase.Instance.DatabaseBuilder
import com.example.getcellinfos.appDatabase.Instance.DatabaseManager
import com.example.getcellinfos.appDatabase.InOutPackage.InOutData
import com.example.getcellinfos.listener.LocationManagerAdvanced
import com.example.getcellinfos.overallService.CellInfoListener
import com.example.getcellinfos.overallService.OverAllClass
import com.example.getcellinfos.overallService.StrengthListener
import com.example.getcellinfos.threadActivity.timerTask
import java.lang.Exception
import java.util.*

class PagerActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }
    private val recordStartButton: Button by lazy {
        findViewById(R.id.recordStartButton)
    }
    private val recordEndButton: Button by lazy {
        findViewById(R.id.recordEndButton)
    }
    private val inOutRadioGroup: RadioGroup by lazy {
        findViewById(R.id.inOutRadioGroup)
    }

    private var inOut: String = ""

    private lateinit var databaseManager: DatabaseManager

    private lateinit var adapter: PagerAdapter
    private lateinit var pagerOverallClass: OverAllClass

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)

        initForActivity()
    }

    private fun initForActivity() {
        initView()
        initBackground()
    }

    private fun initBackground() {
        initDatabaseManager()
        initOverallClass()
    }

    private fun initView() {
        initViewPager()
        initButton()
    }

    private fun initOverallClass() {
        pagerOverallClass = OverAllClass(this)
    }

    private fun initViewPager() {
        adapter = PagerAdapter(this)
        viewPager.adapter = adapter
    }

    private fun initDatabaseManager() {
        databaseManager = DatabaseBuilder(this).getInstance("inout")!!
    }

    private fun initButton() {

        recordStartButton.setOnClickListener {
            try {
                addInOutDataToDb(1)
                Toast.makeText(this, "record start", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        recordEndButton.setOnClickListener {
            try {
                timer?.cancel()
                timer = null
                timerTask = null
                Toast.makeText(this, "record stop", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addInOutDataToDb(autotime: Int) {
        if (timer == null) {
            timer = Timer()
        }

        if (timerTask == null) {
            timerTask = timerTask {
                updateDB()
            }
        }

        timer?.schedule(timerTask, 1000 * autotime.toLong(), 1000 * autotime.toLong())
    }

    override fun onResume() {
        super.onResume()
        pagerOverallClass.locationService()
            .listenForLocationUpdate(LocationManager.NETWORK_PROVIDER, LocationManagerAdvanced(
                update = { _, _, _ -> }, changeMap = { _, _ -> }
            ))
        pagerOverallClass.cellService()
            .listenForCellUpdate(StrengthListener { }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
        pagerOverallClass.cellService().listenForCellUpdate(CellInfoListener(updateView = { },
            updateAdapter = { },
            updateMap = { _, _ -> },
            callStation = false
        ), PhoneStateListener.LISTEN_CELL_INFO
        )
    }

    private fun updateDB() {
        inOut = when (inOutRadioGroup.checkedRadioButtonId) {
            R.id.indoor_radioButton -> "실내"
            else -> "실외"
        }

        databaseManager.insert(
            InOutData(
                uid = null,
                date = pagerOverallClass.getCurrentTimeFromFormat("yyyy-MM-dd"),
                time = pagerOverallClass.getCurrentTimeFromFormat("hh:mm:ss"),
                latitude = pagerOverallClass.locationService().getLocation()[0].toString(),
                longitude = pagerOverallClass.locationService().getLocation()[1].toString(),
                altitude = pagerOverallClass.locationService().getLocation()[2].toString(),
                rsrp = pagerOverallClass.cellService().getCellList()[0],
                rsrq = pagerOverallClass.cellService().getCellList()[1],
                rssi = pagerOverallClass.cellService().getCellList()[2],
                rssnr = pagerOverallClass.cellService().getCellList()[3],
                earfcn = pagerOverallClass.cellService().getCellList()[4],
                pci = pagerOverallClass.cellService().getCellList()[5],
                neighborCell = pagerOverallClass.cellService().getCellList()[6],
                inOut = inOut,
                wifi_powerful_strength = adapter.getWifiStrength(),
                satellite_strengths = adapter.getGpsSatelliteStrength().toString(),
                ssrsrp = adapter.getNrStregnth()[0],
                ssrsrq = adapter.getNrStregnth()[1],
                sssinr = adapter.getNrStregnth()[2],
                light = adapter.getLightPower()
            )
        )
    }

    override fun onStop() {
        super.onStop()
        timer = null
        timerTask = null
        pagerOverallClass.locationService().stopLocationUpdate()
        pagerOverallClass.cellService().stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_fragment_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clearInOutTable -> {
                databaseManager.deleteAll()
            }
            R.id.inOutSave -> {
                databaseManager.exportCSV(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}