package com.bannanguy.task1androidapp.ui.cityDetail

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bannanguy.task1androidapp.data.api.weather.WeatherAPI
import com.bannanguy.task1androidapp.data.CityDataSource
import com.bannanguy.task1androidapp.data.CityDetailWeatherInfo
import com.bannanguy.task1androidapp.utils.ConfigPropertiesUtils
import java.io.File

class CityDetailViewModel(
    private val dataSource: CityDataSource,
    private val cacheDir: File
) : ViewModel() {

    private val cityDetailInfoLiveData: MutableLiveData<CityDetailWeatherInfo> by lazy {
        MutableLiveData<CityDetailWeatherInfo>()
    }

    fun loadWeatherDataOfCity(city_id: Long) {

        val apiKey = ConfigPropertiesUtils.getValue("api")

        if (apiKey == null) {
            Log.e("loadWeatherDataOfCity", "Error: API key is null.")
            return
        }

        val currentCity = dataSource.getCityForId(city_id)

        if (currentCity == null) {
            Log.e("loadWeatherDataOfCity", "Passed city id that isn't exist at dataSource.")
            return
        }

        WeatherAPI.getWeatherByCity(
            cacheDir,
            currentCity
        ) { weatherResponse ->
            val cityDetailWeatherInfo = CityDetailWeatherInfo(
                currentCity.id,
                currentCity.name,
                weatherResponse.current.temp_c,
                weatherResponse.current.condition.icon,
                weatherResponse.current.condition.text,
                weatherResponse.current.wind_kph,
                weatherResponse.current.wind_dir
            )

            cityDetailInfoLiveData.postValue(cityDetailWeatherInfo)
        }

    }

    fun observeLiveData() : LiveData<CityDetailWeatherInfo> {
        return cityDetailInfoLiveData
    }
}

class CityDetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CityDetailViewModel(
                dataSource = CityDataSource.getDataSource(context.resources),
                cacheDir = context.cacheDir
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}