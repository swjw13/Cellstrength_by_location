package com.example.getcellinfos.Pager.fragments.WifiClass

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.getcellinfos.R

class WifiRecyclerViewAdapter: RecyclerView.Adapter<WifiRecyclerViewAdapter.ViewHolder>() {

    var list = mutableListOf<WifiDataUnit>()

    inner class ViewHolder(private var itemview: View): RecyclerView.ViewHolder(itemview){
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bind(item: WifiDataUnit){
            itemview.findViewById<TextView>(R.id.wifiNameTextView).text = item.name
            itemview.findViewById<TextView>(R.id.wifiBssidTextView).text = "[${item.subName}]"
            itemview.findViewById<TextView>(R.id.wifiWidthTextView).text = when(item.bandwidth){
                0 -> "bandwidth: 20Mhz"
                1 -> "bandwidth: 40Mhz"
                2 -> "bandwidth: 80Mhz"
                3 -> "bandwidth: 160Mhz"
                else -> "bandwidth: (80+80)Mhz"
            }
            itemview.findViewById<TextView>(R.id.wifiStrengthTextView).text = "rssi: ${item.strength}"
            itemview.findViewById<TextView>(R.id.wifiFrequencyTextView).text = "frequency: ${item.frequency}"

            itemview.findViewById<CardView>(R.id.wifiItemCardView).setBackgroundColor(when(item.strength){
                in -70 .. 0 -> Color.GREEN
                in -85 .. -70 -> Color.YELLOW
                in -100 .. -85 -> Color.rgb(255,87,34)
                else -> Color.BLACK
            })

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.wifi_list_recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}