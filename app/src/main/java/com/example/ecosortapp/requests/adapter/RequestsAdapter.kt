package com.example.ecosortapp.requests.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecosortapp.databinding.RequestItemsBinding
import com.example.ecosortapp.requests.model.RequestData

class RequestsAdapter(
    private val list: MutableList<RequestData>,
    val clickListener: OnRequestClickListener
) :
    RecyclerView.Adapter<RequestsAdapter.RequestViewHolder>() {

    inner class RequestViewHolder(val requestItemsBinding: RequestItemsBinding) :
        RecyclerView.ViewHolder(requestItemsBinding.root) {
        fun setData(request: RequestData, action: OnRequestClickListener) {
            requestItemsBinding.apply {
                client.text = request.client
                weight.text = request.weight
            }
            requestItemsBinding.root.setOnClickListener {
                action.onRequestClick(request, adapterPosition)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        return RequestViewHolder(
            RequestItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = list[position]
        holder.setData(request, clickListener)

    }

    interface OnRequestClickListener {
        fun onRequestClick(request: RequestData, position: Int)
    }
}