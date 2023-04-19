package com.myprojects.data.local

import com.myprojects.data.entity.AlertEntity
import com.myprojects.data.entity.CashedEntity
import com.myprojects.data.entity.FavoriteEntity

interface LocalSource {

    suspend fun insertFavorite(favoriteEntity: FavoriteEntity)

    suspend fun deleteFavorite(favoriteEntity: FavoriteEntity)

    suspend fun updateFavorite(favoriteEntity: FavoriteEntity)

    suspend fun getAllFavorites(): List<FavoriteEntity>

    suspend fun insertCashed(cashed: CashedEntity)

    suspend fun deleteCashed(cashed: CashedEntity)

    suspend fun updateCashed(cashed: CashedEntity)

    suspend fun getAllCashed(): List<CashedEntity>

    suspend fun insertAlert(alertEntity: AlertEntity)

    suspend fun deleteAlert(alertEntity: AlertEntity)

    suspend fun updateAlert(alertEntity: AlertEntity)

    suspend fun getAllAlerts() : List<AlertEntity>

}