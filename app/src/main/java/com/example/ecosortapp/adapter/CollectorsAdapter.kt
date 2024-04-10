package com.example.ecosortapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecosortapp.admin.model.CollectorData
import com.example.ecosortapp.databinding.CollectorItemBinding

class CollectorsAdapter(
    private val list: MutableList<CollectorData>,
    private val clickListener: OnCollectorClickListener
) :
    RecyclerView.Adapter<CollectorsAdapter.CollectorsViewHolder>() {

    inner class CollectorsViewHolder(private val collectorItemBinding: CollectorItemBinding) :
        RecyclerView.ViewHolder(collectorItemBinding.root) {
        fun setData(collector: CollectorData) {
            collectorItemBinding.apply {
                collectorName.text = collector.collectorName
                collectorLocation.text = collector.collectorLocation
            }
            collectorItemBinding.root.setOnClickListener {
                clickListener.onCollectorClick(collector, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorsViewHolder {
        return CollectorsViewHolder(
            CollectorItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CollectorsViewHolder, position: Int) {
        val collector = list[position]
        holder.setData(collector)
    }

    interface OnCollectorClickListener {
        fun onCollectorClick(collector: CollectorData, position: Int)
    }
}