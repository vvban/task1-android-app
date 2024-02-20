package com.bannanguy.task1androidapp.data

data class WeatherResponse(
    val location: LocationInfo,
    val current: CurrentInfo
)

data class LocationInfo(
    val name: String
)

data class CurrentInfo(
    val temp_c: Double
)