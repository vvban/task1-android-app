package com.bannanguy.task1androidapp.ui.cityList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bannanguy.task1androidapp.api.WeatherInstance
import com.bannanguy.task1androidapp.data.CityInfo
import com.bannanguy.task1androidapp.data.WeatherResponse
import com.bannanguy.task1androidapp.utils.ConfigPropertiesUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val citiesInfoLiveData: MutableLiveData<List<CityInfo>> by lazy {
        MutableLiveData<List<CityInfo>>()
    }


    fun getWeather() {

        val apiKey = ConfigPropertiesUtils.getValue("api")

        if (apiKey == null) {
            println("Error: API key is null.")
            return
        }

        WeatherInstance.api.fetchWeather(
            apiKey,
            "London"
        ).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                response.body()?.let { weatherResponse ->

                    // FIXME: temp
                    var citiesInfoList = List(3) {
                        CityInfo(
                            weatherResponse.location.name,
                            weatherResponse.current.temp_c
                        )
                        CityInfo(
                            weatherResponse.location.name,
                            weatherResponse.current.temp_c
                        )
                        CityInfo(
                            weatherResponse.location.name,
                            weatherResponse.current.temp_c
                        )
                    }

                    Log.d("MainViewModel", citiesInfoList.toString())

                    citiesInfoLiveData.postValue(citiesInfoList)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("MainViewModel", t.message.toString())
            }

        })
    }

    fun observeLiveData() : LiveData<List<CityInfo>> {
        return citiesInfoLiveData
    }

}