package com.myprojects.util

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.myprojects.data.entity.FavoriteEntity
import com.myprojects.data.preferences.AppUnits
import com.myprojects.data.preferences.PreferenceProvider
import com.myprojects.model.AlertModel
import com.myprojects.model.FavoriteModel
import com.myprojects.model.weather.Daily
import com.myprojects.model.weather.Hourly
import com.myprojects.model.weather.Temp
import com.myprojects.ui.adpater.DayAdapter
import com.myprojects.ui.adpater.FavoriteAdapter
import com.myprojects.ui.adpater.WeekAdapter
import com.myprojects.weatherwise.R
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("setWeatherIcon")
fun ImageView.setWeatherIcon(icon: String) {
    this.setImageDrawable(getDrawableIcon(context, icon))
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setTempDegree")
fun TextView.setTempDegree(degree: Double) {
    val pref = PreferenceProvider(context)
    when (pref.getTempUnit()) {
        AppUnits.FAHRENHEIT.string -> {
            this.text =
                "${fromKelvinToFahrenheit(degree).toInt()}" + "°" + "F"
        }
        AppUnits.CELSIUS.string -> {
            this.text =
                "${fromKelvinToCelsius(degree).toInt()}" + "°" + "C"
        }
        else -> {
            this.text = "${degree.toInt()}" + "°" + "K"
        }
    }
}

@BindingAdapter("setCityName")
fun TextView.setCityName(latLng: LatLng){
    val pref  = PreferenceProvider(context)
    val geocoder = Geocoder(context.applicationContext, Locale(pref.getLanguage()))
    val address: MutableList<Address>? =
        geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
    this.text = address?.get(0)?.adminArea ?: ""
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setWindSpeed")
fun TextView.setWindSpeed(windSpeed: Double) {
    val pref = PreferenceProvider(context)
    when (pref.getWindSpeedUnit()) {
        AppUnits.METER_BY_SECOND.string -> {
            this.text = "${windSpeed.toInt()}M/S"
        }
        AppUnits.MILE_BY_HOUR.string -> {
            this.text = "${fromMeterBySecToMileByHour(windSpeed).toInt()}M/H"
        }
    }
}

@BindingAdapter("setDayAdapter")
fun RecyclerView.setDayAdapter(list: ArrayList<Hourly>?) {
    this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    val dayAdapter = DayAdapter(context)
    dayAdapter.setList(list ?: arrayListOf())
    this.adapter = dayAdapter
}

@BindingAdapter("setWeekAdapter")
fun RecyclerView.setWeekAdapter(list: ArrayList<Daily>?) {
    this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    val weekAdapter = WeekAdapter()
    weekAdapter.setWeek(list ?: arrayListOf())
    this.adapter = weekAdapter
}

@BindingAdapter("setFavoriteAdapter")
fun RecyclerView.setFavoriteAdapter(favModel: FavoriteModel?) {
    if (favModel != null) {
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val favAdapter = FavoriteAdapter(listener = favModel.favAdapterInterface)
        favAdapter.setCountries(favModel.countries as ArrayList<FavoriteEntity>)
        this.adapter = favAdapter
    }
}
/*
@BindingAdapter("setAlertAdapter")
fun RecyclerView.setAlertAdapter(alertModel: AlertModel?) {
    if (alertModel != null) {
        this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val alertAdapter = AlertAdapter(listener = alertModel.listener)
        alertAdapter.setList(alertModel.list ?: arrayListOf())
        this.adapter = alertAdapter
    }
}*/

@SuppressLint("SimpleDateFormat")
@BindingAdapter("setWeekTxt")
fun TextView.setWeekTxt(msDate: Long) {
    val date = Date(msDate * 1000L)
    val format = SimpleDateFormat("EEE")
    this.text = (format.format(date))
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("setDayAndMonthDate")
fun TextView.setDayAndMonthDate(msDate: Long) {
    val date = Date(msDate * 1000L)
    val format = SimpleDateFormat("dd MMM")
    this.text = (format.format(date))
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setMinAndMaxDegree")
fun TextView.setMinAndMaxDegree(temp: Temp) {
    val pref = PreferenceProvider(context)
    when (pref.getTempUnit()) {
        AppUnits.FAHRENHEIT.string -> {
            this.text =
                "${fromKelvinToFahrenheit(temp.min).toInt()}/${fromKelvinToFahrenheit(temp.max).toInt()}" + context.getString(
                    R.string.degree_symble
                ) + "F"
        }
        AppUnits.CELSIUS.string -> {
            this.text =
                "${fromKelvinToCelsius(temp.min).toInt()}/${fromKelvinToCelsius(temp.max).toInt()}" + context.getString(
                    R.string.degree_symble
                ) + "C"
        }
        else -> {
            this.text =
                "${temp.min.toInt()}/${temp.max.toInt()}" + context.getString(R.string.degree_symble) + "K"
        }
    }
}


@SuppressLint("SimpleDateFormat", "SetTextI18n")
@BindingAdapter("setStartDate")
fun TextView.setStartDate(long: Long) {
    val format = SimpleDateFormat("dd MMM")
    this.text = "From: ${format.format(long)}"
}

@SuppressLint("SimpleDateFormat", "SetTextI18n")
@BindingAdapter("setEndDate")
fun TextView.setEndDate(long: Long) {
    val format = SimpleDateFormat("dd MMM")
    this.text = "From: ${format.format(long)}"
}

@SuppressLint("SimpleDateFormat", "SetTextI18n")
@BindingAdapter("setStartTime")
fun TextView.setStartTime(long: Long) {
    val format = SimpleDateFormat("hh:mm aaa")
    this.text = "At: ${format.format(long)}"
}

@SuppressLint("SimpleDateFormat", "SetTextI18n")
@BindingAdapter("setEndTime")
fun TextView.setEndTime(long: Long) {
    val format = SimpleDateFormat("hh:mm aaa")
    this.text = format.format(long)
}


















