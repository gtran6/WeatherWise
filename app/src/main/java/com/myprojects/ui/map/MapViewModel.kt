package com.myprojects.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.myprojects.data.entity.CashedEntity
import com.myprojects.data.entity.FavoriteEntity
import com.myprojects.data.preferences.AppUnits
import com.myprojects.data.remote.Resource
import com.myprojects.repository.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(private val repositoryInterface: RepositoryInterface) : ViewModel() {

    fun saveMyLatLon(latlon: LatLng) = repositoryInterface.setLatLon(latlon)

    fun getMyLatLon() = repositoryInterface.getLatLon()

    fun updateCashedData(cashedEntity: CashedEntity) =
        viewModelScope.launch { repositoryInterface.insertCashed(cashedEntity) }


    fun addFavoriteTodatabase(fav: FavoriteEntity) {
        viewModelScope.launch {
            repositoryInterface.insertFavorite(fav)
        }
    }

    fun getWeatherRemotlyLatlon(
        latLng: LatLng = LatLng(
            30.02401127333763,
            31.564412713050846
        ),
        language: String = AppUnits.EN.string
    ) = liveData(Dispatchers.IO) {
        emit(Resource.Loading(isLoading = true, _data = null))
        try {
            emit(Resource.Success(_data = repositoryInterface.getWeatherByLatLon(latLng, language)))
        } catch (exception: Exception) {
            emit(
                Resource.Error(
                    exception.message ?: "Something wrong happened",
                )
            )
        }
    }
    fun getLang() = repositoryInterface.getLanguage()

}