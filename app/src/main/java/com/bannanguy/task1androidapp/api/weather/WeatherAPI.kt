package com.bannanguy.task1androidapp.api.weather

import android.util.Log
import com.bannanguy.task1androidapp.data.CityData
import com.bannanguy.task1androidapp.data.WeatherResponse
import com.bannanguy.task1androidapp.utils.ConfigPropertiesUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherAPI {
    companion object {

        fun getWeatherByCity(
            cityData: CityData,
            onResponseCallback: (WeatherResponse) -> Unit
        ) {

            val apiKey = ConfigPropertiesUtils.getValue("api")

            if (apiKey == null) {
                Log.e("loadWeatherDataOfCity", "Error: API key is null.")
                return
            }

            WeatherInstance.api.fetchSingleCityWeather(
                apiKey,
                cityData.lat.toString() + "," + cityData.lon.toString()
            ).enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    response.body()?.let { weatherResponse ->

                        onResponseCallback(weatherResponse)

                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.e("CityDetailViewModel", t.message.toString())
                }

            })
        }

    }
}