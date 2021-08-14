package com.example.getcellinfos.Pager.FiveG

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.getcellinfos.R

class FiveGFragment(val context: FragmentActivity) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_five_gactivity, container, false)
    }

    override fun onResume() {
        super.onResume()

    }
}