package com.example.getcellinfos.Pager.fragments.SensorActivity

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

class SensorListener(val eventChange: (FloatArray?) -> Unit, val accChange: (Int) -> Unit): SensorEventListener {

    override fun onSensorChanged(event: SensorEvent?) {
        eventChange(event?.values)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        accChange(accuracy)
    }
}