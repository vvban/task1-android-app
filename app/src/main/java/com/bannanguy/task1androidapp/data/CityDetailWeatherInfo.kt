package com.bannanguy.task1androidapp.data

data class CityDetailWeatherInfo(
    val city_id: Long,
    val name: String,
    val temp_c: Double,
    val condition_icon_uri: String,
    val condition_text: String,
    val wind_kph: Double,
    val wind_dir: String
)