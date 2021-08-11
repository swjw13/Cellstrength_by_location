package com.example.getcellinfos.WifiClass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.getcellinfos.R

class WifiRecyclerViewAdapter: RecyclerView.Adapter<WifiRecyclerViewAdapter.ViewHolder>() {

    var list = mutableListOf<WifiDataUnit>()

    inner class ViewHolder(val itemview: View): RecyclerView.ViewHolder(itemview){
        fun bind(item: WifiDataUnit){
            itemview.findViewById<TextView>(R.id.wifiNameTextView).text = item.name
            itemview.findViewById<TextView>(R.id.wifiBssidTextView).text = item.subName
            itemview.findViewById<TextView>(R.id.wifiStrengthTextView).text = item.strength.toString()
            itemview.findViewById<TextView>(R.id.wifiCapabilityTextView).text = item.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wifi_list_recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}