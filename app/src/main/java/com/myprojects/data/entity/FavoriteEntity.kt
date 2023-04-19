package com.myprojects.data.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.myprojects.model.weather.WeatherResponse

@Entity(tableName = "favorite_table")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int = 0,
    var latLng: LatLng,
    var locationName: String,
    var cashedData: WeatherResponse
)