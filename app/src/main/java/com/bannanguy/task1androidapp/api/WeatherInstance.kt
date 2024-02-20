package com.bannanguy.task1androidapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object WeatherInstance {

    val api : WeatherService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)
    }
}