package com.myprojects.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.myprojects.data.remote.Resource
import com.myprojects.repository.RepositoryInterface
import kotlinx.coroutines.Dispatchers

class HomeViewModel(private val repositoryInterface: RepositoryInterface) : ViewModel() {
    fun getCashedData() = liveData(Dispatchers.IO) {
        emit(Resource.Loading(isLoading = true, _data = null))
        try {
            emit(Resource.Success(_data = repositoryInterface.getAllCashed()))
        } catch (exception: Exception) {
            emit(
                Resource.Error(
                    exception.message ?: "SomethingWong happened",
                )
            )
        }
    }
}