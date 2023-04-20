package com.myprojects.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myprojects.data.DAO.AlertDao
import com.myprojects.data.DAO.CashedDao
import com.myprojects.data.DAO.FavoriteDao
import com.myprojects.data.entity.AlertEntity
import com.myprojects.data.entity.CashedEntity
import com.myprojects.data.entity.FavoriteEntity

@Database(entities = [CashedEntity::class, FavoriteEntity::class, AlertEntity::class], version = 1)
@TypeConverters(RoomConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    abstract fun cashedDao(): CashedDao

    abstract fun alertDao(): AlertDao
}
