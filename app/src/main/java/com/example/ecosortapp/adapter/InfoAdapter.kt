package com.example.ecosortapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecosortapp.databinding.InfoItemBinding
import com.example.ecosortapp.info.model.BlogData

class InfoAdapter(
    private val list: MutableList<BlogData>,
    val clickListener: OnInfoClickListener
) : RecyclerView.Adapter<InfoAdapter.InfoViewModel>() {

    inner class InfoViewModel(val infoItemBinding: InfoItemBinding) :
        RecyclerView.ViewHolder(infoItemBinding.root) {
        fun setData(
            info: BlogData,
            action: OnInfoClickListener
        ) {
            infoItemBinding.apply {
                infoTitle.text = info.blogTitle
                infoContent.text = info.blogContent
            }
            infoItemBinding.root.setOnClickListener {
                action.onInfoClick(info, adapterPosition)
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewModel {
        return InfoViewModel(
            InfoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: InfoViewModel, position: Int) {
        val info = list[position]
        holder.setData(info, clickListener)

    }

    interface OnInfoClickListener {
        fun onInfoClick(info: BlogData, position: Int)
    }

}