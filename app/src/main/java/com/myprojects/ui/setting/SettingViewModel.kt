package com.myprojects.ui.setting

import androidx.lifecycle.ViewModel
import com.myprojects.repository.RepositoryInterface

class SettingViewModel(private val repositoryInterface: RepositoryInterface) : ViewModel() {

    fun setTempUnit(string: String) = repositoryInterface.setTempUnit(string)

    fun getTempUnit() = repositoryInterface.getTempUnit()

    fun setWindSpeedUnit(string: String) = repositoryInterface.setWindSpeedUnit(string)

    fun getWindSpeedUnit() = repositoryInterface.getWindSpeedUnit()

    fun getLanguage() = repositoryInterface.getLanguage()

    fun setLanguage (string: String) = repositoryInterface.setLanguage(string)
}