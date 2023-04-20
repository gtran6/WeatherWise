package com.myprojects.model

import com.myprojects.data.entity.FavoriteEntity
import com.myprojects.ui.adpater.FavoriteAdapter

data class FavoriteModel(
    val favAdapterInterface: FavoriteAdapter.FavoriteAdapterInterface,
    val countries: List<FavoriteEntity>
)
