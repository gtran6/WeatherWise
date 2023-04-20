package com.myprojects.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.myprojects.data.entity.FavoriteEntity
import com.myprojects.data.remote.Resource
import com.myprojects.repository.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repositoryInterface: RepositoryInterface) : ViewModel() {

    fun getFavoriteList() = liveData(Dispatchers.IO) {
        emit(Resource.Loading(isLoading = true, _data = null))
        try {
            emit(Resource.Success(_data = repositoryInterface.getAllFavorites()))
        } catch (exception: Exception) {
            emit(
                Resource.Error(
                    exception.message ?: "SomethingWong happened",
                )
            )
        }
    }

    fun deleteFromFavorite(favoriteEntity: FavoriteEntity) {
        viewModelScope.launch {
            repositoryInterface.deleteFavorite(favoriteEntity)
        }
    }
}