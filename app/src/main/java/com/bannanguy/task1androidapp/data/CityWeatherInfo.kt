package com.bannanguy.task1androidapp.data

import androidx.lifecycle.MutableLiveData

data class CityWeatherInfo(
    val city_id: Long,
    val name: String,
    val temp: MutableLiveData<Double>
)