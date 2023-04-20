package com.myprojects.model

import com.myprojects.model.weather.Daily
import com.myprojects.model.weather.Hourly

data class ReportModel(
    val week: ArrayList<Daily>,
    val day: ArrayList<Hourly>
)