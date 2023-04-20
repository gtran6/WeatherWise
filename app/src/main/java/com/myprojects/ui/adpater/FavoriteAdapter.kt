package com.myprojects.ui.adpater

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myprojects.data.entity.FavoriteEntity
import com.myprojects.weatherwise.databinding.FavoriteItemBinding

class FavoriteAdapter(
    private val countries: ArrayList<FavoriteEntity> = arrayListOf(),
    private val listener: FavoriteAdapterInterface
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setCountries(newList: ArrayList<FavoriteEntity>) {
        countries.clear()
        countries.addAll(newList)
        notifyDataSetChanged()
    }

    interface FavoriteAdapterInterface {
        fun onDeleteImageClick(pos: Int)
        fun onItemClick(pos: Int)
    }

    class ViewHolder(private val binding: FavoriteItemBinding, listener: FavoriteAdapterInterface) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imgDelete.setOnClickListener { listener.onDeleteImageClick(adapterPosition) }
            binding.parent.setOnClickListener { listener.onItemClick(adapterPosition) }
        }

        fun bind(country: String) {
            binding.data = country
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        FavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener)

    override fun getItemCount(): Int = if (countries.isEmpty()) 0 else countries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(countries[position].locationName)
}