package com.myprojects.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myprojects.data.entity.FavoriteEntity
import com.myprojects.weatherwise.databinding.FavoriteItemBinding

class FavoriteAdapter(
    private val countries: ArrayList<FavoriteEntity> = arrayListOf(),
    private val listener: FavAdapterInterface
) :
    RecyclerView.Adapter<FavoriteAdapter.FavViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setCountries(newList: ArrayList<FavoriteEntity>) {
        countries.clear()
        countries.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder =
        FavViewHolder(
            FavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener
        )

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) =
        holder.bind(countries[position].locationName)

    override fun getItemCount(): Int = if (countries.isEmpty()) 0 else countries.size

    inner class FavViewHolder(val binding: FavoriteItemBinding, listener: FavAdapterInterface) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imgDelete.setOnClickListener { listener.onDeleteImageClick(adapterPosition) }
            binding.parent.setOnClickListener { listener.onItemClick(adapterPosition) }
        }

        fun bind(country: String) {
            binding.data = country
            binding.executePendingBindings()
        }
    }

    interface FavAdapterInterface {
        fun onDeleteImageClick(pos: Int)
        fun onItemClick(pos: Int)
    }
}