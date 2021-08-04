package com.example.getcellinfos.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.getcellinfos.MainActivity
import com.example.getcellinfos.R
import com.example.getcellinfos.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private var autoUpdateSecond = 0
    private var checkedSetting = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.radioGroup.clearCheck()

        binding.admitButton.setOnClickListener {
            checkedSetting = binding.radioGroup.checkedRadioButtonId

            when(checkedSetting){
                R.id.noTimerRadio -> {
                    checkedSetting = 1
                    intentAction()
                }
                R.id.timerRadio -> {
                    checkedSetting = 2
                    makeDialog()
                }
            }
        }
    }

    private fun makeDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.time_dialog)

        dialog.findViewById<Button>(R.id.submitButton).setOnClickListener {
            autoUpdateSecond = dialog.findViewById<EditText>(R.id.secondEditText).text.toString().toInt()
            Toast.makeText(this, "입력 확인되었습니다!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            intentAction()
        }
        dialog.show()
    }

    private fun intentAction(){
        val intent = Intent(this, MainActivity::class.java)

        intent.putExtra("settingNumber", checkedSetting)
        intent.putExtra("autoTime", autoUpdateSecond)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
//        finish()
    }

}