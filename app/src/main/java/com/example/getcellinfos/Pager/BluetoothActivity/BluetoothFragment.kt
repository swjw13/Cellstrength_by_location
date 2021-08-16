package com.example.getcellinfos.Pager.BluetoothActivity

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.getcellinfos.R
import java.lang.Exception

class BluetoothFragment(val context: FragmentActivity) : Fragment() {

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var scanCallback: ScanCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bluetoothManager = context.getSystemService(AppCompatActivity.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        scanCallback = object : ScanCallback() {
            @SuppressLint("SetTextI18n")
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                super.onScanResult(callbackType, result)
                Log.d("jae", "onScanResult: $result, callback type: $callbackType")
                view?.findViewById<TextView>(R.id.bluetooth_first)?.text = result.toString()
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                super.onBatchScanResults(results)
                if(results != null){
                    Log.d("jae", "onBatchScanResults: $results")
                }
            }

            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
                Log.d("jae", "onScanFailed: $errorCode")
            }
        }
        return inflater.inflate(R.layout.activity_bluetooth, container, false)
    }

    override fun onResume() {
        super.onResume()

        try {
            bluetoothAdapter.bluetoothLeScanner.startScan(scanCallback)
        } catch (e: Exception){
            Log.d("jae", e.message.toString())
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        bluetoothAdapter.bluetoothLeScanner.stopScan(scanCallback)
    }
}