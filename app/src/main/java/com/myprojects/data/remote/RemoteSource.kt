package com.myprojects.data.remote

import com.google.android.gms.maps.model.LatLng
import com.myprojects.model.weather.WeatherResponse

interface RemoteSource {

    suspend fun getWeatherByLatAndLing(latLng: LatLng, language: String): WeatherResponse
}