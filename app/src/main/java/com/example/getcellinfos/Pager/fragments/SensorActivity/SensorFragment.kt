package com.example.getcellinfos.Pager.fragments.SensorActivity

import android.annotation.SuppressLint
import android.hardware.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.getcellinfos.R

@SuppressLint("SetTextI18n")
class SensorFragment(val context: FragmentActivity) : Fragment() {

    private lateinit var sensorManager: SensorManager
    private lateinit var tempSensorListener: SensorEventListener

    var light_strength = 0F

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sensorManager = context.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        tempSensorListener =
            SensorListener({ value -> updateValue(1, value) }, { acc -> updateAcc(1, acc) })

        return inflater.inflate(R.layout.activity_sensor, container, false)
    }

    private fun updateValue(type: Int, value: FloatArray?) {
        when (type) {
            1 -> {
                light_strength = value?.get(0) ?: 0F
                view?.findViewById<TextView>(R.id.temp_result)?.text = "light: $light_strength"
            }
        }
    }

    private fun updateAcc(type: Int, acc: Int) {

        when (type) {
            1 -> {
                view?.findViewById<TextView>(R.id.temp_acc)?.text = "Accuracy: ${
                    when (acc) {
                        3 -> "High"
                        2 -> "Medium"
                        1 -> "Low"
                        0 -> "Unreliable"
                        else -> "No Contact"
                    }
                }"
            }
        }
    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(
            tempSensorListener,
            sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(tempSensorListener)
    }
}