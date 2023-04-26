package com.myprojects.ui.alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.myprojects.data.entity.AlertEntity
import com.myprojects.data.remote.Resource
import com.myprojects.repository.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertViewModel(val repo: RepositoryInterface) : ViewModel() {
    fun insetAlert(alertEntity: AlertEntity) {
        viewModelScope.launch {
            repo.insertAlert(alertEntity)
        }
    }

    fun getAllAlerts = liveData(Dispatchers.IO) {
        emit(Resource.Loading(isLoading = true, _data = null))
        try {
            emit(Resource.Success(_data = repo.getAllAlerts()))
        } catch (e: Exception) {
            Resource.Error(exception = e.message ?: "Error")
        }
    }

    fun removeAlert(alertEntity: AlertEntity) {
        viewModelScope.launch {
            repo.deleteAlert(alertEntity)
        }
    }
}