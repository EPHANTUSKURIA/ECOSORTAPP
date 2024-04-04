package com.example.ecosortapp.requests.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecosortapp.databinding.RequestItemsBinding
import com.example.ecosortapp.requests.model.RequestData

class RequestsAdapter(
    private val list: MutableList<RequestData>,
    private val clickListener: OnRequestClickListener
) : RecyclerView.Adapter<RequestsAdapter.RequestViewHolder>() {

    inner class RequestViewHolder(
        private val binding: RequestItemsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(request: RequestData) {
            with(binding) {
                client.text = request.client
                weight.text = request.weight
                tvTime.text = request.tvTime
                mtrlCalendarDaysOfWeek.text = request.tvSelectDate

                root.setOnClickListener {
                    clickListener.onRequestClick(request, adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val binding = RequestItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = list[position]
        holder.bind(request)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnRequestClickListener {
        fun onRequestClick(request: RequestData, position: Int)
    }
}

