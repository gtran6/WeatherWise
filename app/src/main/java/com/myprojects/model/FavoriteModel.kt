package com.myprojects.model

import com.myprojects.data.entity.FavoriteEntity
import com.myprojects.ui.adapter.FavoriteAdapter

data class FavoriteModel(
    val favAdapterInterface: FavoriteAdapter.FavAdapterInterface,
    val countries: List<FavoriteEntity>
)
