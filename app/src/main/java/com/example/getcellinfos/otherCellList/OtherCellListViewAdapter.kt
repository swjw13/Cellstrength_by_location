package com.example.getcellinfos.otherCellList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.getcellinfos.databinding.OtherCellRecyclerviewItemBinding

@SuppressLint("SetTextI18n")
class OtherCellListViewAdapter: RecyclerView.Adapter<OtherCellListViewAdapter.ViewHolder>() {

    var list = mutableListOf<OtherCells>()

    inner class ViewHolder(val item: OtherCellRecyclerviewItemBinding): RecyclerView.ViewHolder(item.root){
        fun bind(otherCells: OtherCells){
            item.otherCellPciTextView.text = "PCI: ${otherCells.pci}"
            item.otherCellEarfcnTextView.text = "Earfcn: ${otherCells.earfcn}"
            item.otherCellRsrpTextView.text = "Rsrp: ${otherCells.rsrp}"
            item.otherCellRsrqTextView.text = "Rsrq: ${otherCells.rsrq}"
            item.otherCellRssiTextView.text = "Rssi: ${otherCells.rssi}"
            item.otherCellRssnrTextView.text = "Rssnr: ${otherCells.rssnr}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(OtherCellRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}