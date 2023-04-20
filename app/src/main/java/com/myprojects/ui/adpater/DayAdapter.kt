package com.myprojects.ui.adpater

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myprojects.model.DayItem
import com.myprojects.model.weather.Hourly
import com.myprojects.util.getTime
import com.myprojects.weatherwise.databinding.DayItemBinding

class DayAdapter(private val context: Context) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    private var data: ArrayList<Hourly> = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newData: ArrayList<Hourly>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder =
        DayViewHolder(DayItemBinding.inflate(LayoutInflater.from(context)))

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) =
        holder.bind(data[position])

    override fun getItemCount(): Int = if (data.isNotEmpty()) data.size else data.size

    inner class DayViewHolder(private var _binding: DayItemBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        fun bind(weather: Hourly) {
            _binding.day =
                DayItem(
                    weather.weather[0].icon,
                    weather.temp.toInt(),
                    getTime(weather.dt)
                )
            _binding.executePendingBindings()
        }
    }
}