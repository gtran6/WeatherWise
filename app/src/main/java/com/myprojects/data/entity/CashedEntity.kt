package com.myprojects.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myprojects.model.weather.WeatherResponse

@Entity(tableName = "cashed_table")
data class CashedEntity(
    @PrimaryKey
    var id: Int = 0,
    var cashedData: WeatherResponse
)