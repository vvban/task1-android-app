package com.bannanguy.task1androidapp.data.api.weather

data class WeatherResponse(
    val location: LocationInfo,
    val current: CurrentInfo
)

data class LocationInfo(
    val name: String
)

data class CurrentInfo(
    val temp_c: Double,
    val condition: Condition,
    val wind_kph: Double,
    val wind_dir: String
)

data class Condition(
    val text: String,
    val icon: String
)