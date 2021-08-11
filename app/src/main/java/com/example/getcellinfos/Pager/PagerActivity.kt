package com.example.getcellinfos.Pager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.getcellinfos.R

class PagerActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }
    private lateinit var adapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)

        initViewPager()
    }
    private fun initViewPager(){
        adapter = PagerAdapter(this)
        viewPager.adapter = adapter
    }
}