package com.myprojects.data.DAO

import androidx.room.*
import com.myprojects.data.entity.AlertEntity

@Dao
interface AlertDao {
    @Update
    fun updateAlert(alertEntity: AlertEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlert(alertEntity: AlertEntity)

    @Delete
    fun deleteAlert(alertEntity: AlertEntity)

    @Query("SELECT * FROM alert_table")
    fun getAllAlerts(): List<AlertEntity>


}