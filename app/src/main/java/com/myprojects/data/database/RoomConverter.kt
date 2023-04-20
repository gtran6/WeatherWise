package com.myprojects.data.database

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.myprojects.model.weather.WeatherResponse

class RoomConverter {

    @TypeConverter
    fun weatherResToJson (value : WeatherResponse) = Gson().toJson(value)

    @TypeConverter
    fun jsonToWeatherRes ( value : String) = Gson().fromJson(value, WeatherResponse::class.java)

    @TypeConverter
    fun latlonToJson (value: LatLng) = Gson().toJson(value)

    @TypeConverter
    fun jsonToLatlon (value : String ) = Gson().fromJson(value, LatLng::class.java)

}