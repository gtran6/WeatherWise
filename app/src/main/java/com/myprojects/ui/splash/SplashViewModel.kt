package com.myprojects.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.myprojects.data.entity.CashedEntity
import com.myprojects.data.preferences.AppUnits
import com.myprojects.data.remote.Resource
import com.myprojects.model.weather.WeatherResponse
import com.myprojects.repository.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SplashViewModel(private val repository: RepositoryInterface) : ViewModel() {
    private val TAG = "SplashViewModel"

    fun getDataFromRepo(
        latLng: LatLng = LatLng(
            32.779167,
            -96.808891
        ),
        language: String = AppUnits.EN.string
    ) = liveData(Dispatchers.IO) {
        Log.d(TAG, "getDataFromRepo: called and its loading ")
        emit(Resource.Loading(isLoading = true, _data = null))
        try {
            emit(Resource.Success(_data = repository.getWeatherByLatLon(latLng, language)))
            Log.d(TAG, "getDataFromRepo: scs")
        } catch (exception: HttpException) {
            emit(
                Resource.Error(
                    exception.message ?: "SomethingWong happened",
                )
            )
        } catch (exception: IOException) {
            emit(
                Resource.Error(
                    exception.message ?: "Are you connected !",
                )
            )
        }
    }

    fun saveResponse(weatherResponse: WeatherResponse) {
        viewModelScope.launch {
            repository.insertCashed(
                CashedEntity(
                    cashedData = weatherResponse
                )
            )
        }
    }

    fun getLatLon() = repository.getLatLon()

    fun setTimeStamp(msTime: Long) = repository.setTimestamp(msTime)

    fun setLatLon(latLng: LatLng) = repository.setLatLon(latLng)

    fun getLang() = repository.getLanguage()
}