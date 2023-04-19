package com.myprojects.data.remote

import com.myprojects.data.preferences.AppUnits
import com.myprojects.model.weather.WeatherResponse
import com.myprojects.util.MY_API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("onecall")
    suspend fun getWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("lang") lang: String = AppUnits.EN.string,
        @Query("appid") appid: String = MY_API_KEY
    ): WeatherResponse
}