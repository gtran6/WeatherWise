package com.myprojects.data.DAO

import androidx.room.*
import com.myprojects.data.entity.FavoriteEntity

@Dao
interface FavoriteDao {

    @Update
    fun updateFav(favoriteEntity: FavoriteEntity)

    @Insert
    fun insertFav(favoriteEntity: FavoriteEntity)

    @Delete
    fun deleteFav(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM favorite_table")
    fun getAllFavorites(): List<FavoriteEntity>
}