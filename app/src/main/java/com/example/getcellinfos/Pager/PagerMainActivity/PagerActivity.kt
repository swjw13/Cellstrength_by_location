package com.example.getcellinfos.Pager.PagerMainActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)

        initViewPager()
        initDatabaseManager()
        initButton()
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
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        recordEndButton.setOnClickListener {
            try {
                timer?.cancel()
                timer = null
                timerTask = null
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

    private fun updateDB() {
        inOut = when (inOutRadioGroup.checkedRadioButtonId) {
            R.id.indoor_radioButton -> "실내"
            else -> "실외"
        }

        databaseManager.insert(
            InOutData(
                uid = null,
                inOut = inOut,
                wifi_powerful_strength = adapter.getWifiStrength(),
                satellite_strengths = adapter.getGpsSatelliteStrength().toString(),
                ssrsrp = adapter.getNrStregnth()[0],
                ssrsrq = adapter.getNrStregnth()[1],
                sssinr = adapter.getNrStregnth()[2]
            )
        )
    }

    override fun onStop() {
        super.onStop()
        timer = null
        timerTask = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_fragment_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.clearInOutTable -> {
                databaseManager.deleteAll()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}