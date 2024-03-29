package com.bannanguy.task1androidapp.data.api.weather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("current.json")
    fun fetchSingleCityWeather(
        @Query("key") apiKey: String,
        @Query("q") city: String,
        @Query("lang") lang: String = "uk"
    ): Call<WeatherResponse>
}