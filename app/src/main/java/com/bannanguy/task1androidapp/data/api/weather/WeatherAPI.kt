package com.bannanguy.task1androidapp.data.api.weather

import android.util.Log
import com.bannanguy.task1androidapp.data.CityData
import com.bannanguy.task1androidapp.data.WeatherResponse
import com.bannanguy.task1androidapp.utils.ConfigPropertiesUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object WeatherAPI {
    fun getWeatherByCity(
        retrofitClient: RetrofitClient,
        cityData: CityData,
        onResponseCallback: (WeatherResponse) -> Unit
    ) {

        val apiKey = ConfigPropertiesUtils.getValue("api")

        if (apiKey == null) {
            Log.e("loadWeatherDataOfCity", "Error: API key is null.")
            return
        }

        val api: WeatherService by lazy {
            retrofitClient.getClient().create(WeatherService::class.java)
        }

        api.fetchSingleCityWeather(
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
                // TODO: Sometimes https://api.weatherapi.com may block you.
                //  How to inform user about it?
                Log.e("WeatherAPI", t.message.toString())
            }

        })
    }

}