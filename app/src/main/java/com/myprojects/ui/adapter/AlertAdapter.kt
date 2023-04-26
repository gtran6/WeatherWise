package com.myprojects.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myprojects.data.entity.AlertEntity
import com.myprojects.weatherwise.databinding.AlertItemBinding

class AlertAdapter(
    val list: ArrayList<AlertEntity> = arrayListOf(),
    val listener: AlertAdapterListener
) : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: ArrayList<AlertEntity>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder =
        AlertViewHolder(AlertItemBinding.inflate(LayoutInflater.from(parent.context)), listener)

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int = if (list.isEmpty()) 0 else list.size
    inner class AlertViewHolder(val binding: AlertItemBinding, listener: AlertAdapterListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.image.setOnClickListener { listener.onDeleteImageClick(adapterPosition) }
        }

        fun bind(alertEntity: AlertEntity) {
            binding.data = alertEntity
            binding.executePendingBindings()
        }
    }

    interface AlertAdapterListener {
        fun onDeleteImageClick(pos: Int)
    }
}