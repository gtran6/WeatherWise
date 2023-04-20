package com.myprojects.model

import com.myprojects.model.weather.Temp

data class WeekModel(
    var date: Long,
    var temp: Temp,
    var icon: String
)
