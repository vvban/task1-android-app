package com.bannanguy.task1androidapp.ui.cityList

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bannanguy.task1androidapp.data.api.weather.WeatherAPI
import com.bannanguy.task1androidapp.data.*
import com.bannanguy.task1androidapp.utils.ConfigPropertiesUtils
import java.io.File

class CitiesListViewModel(
    private val dataSource: CityDataSource,
    private val cacheDir: File
) : ViewModel() {

    private val citiesWeatherInfoLiveData: MutableLiveData<List<CityWeatherInfo>> by lazy {
        MutableLiveData<List<CityWeatherInfo>>()
    }

    fun loadWeatherData() {

        val apiKey = ConfigPropertiesUtils.getValue("api")

        if (apiKey == null) {
            println("Error: API key is null.")
            return
        }

        val listOfCities = dataSource.getCityList()

        val currentListOfCityWeatherInfo: MutableList<CityWeatherInfo> =
            ArrayList<CityWeatherInfo>(0).toMutableList()

        listOfCities.forEach { city ->
            WeatherAPI.getWeatherByCity(
                cacheDir,
                city
            ) { weatherResponse ->
                val cityWeatherInfo = CityWeatherInfo(
                    city.id,
                    weatherResponse.location.name,
                    weatherResponse.current.temp_c
                )

                currentListOfCityWeatherInfo.add(cityWeatherInfo)

                citiesWeatherInfoLiveData.postValue(
                    currentListOfCityWeatherInfo
                )
            }
        }
    }

    fun observeLiveData() : LiveData<List<CityWeatherInfo>> {
        return citiesWeatherInfoLiveData
    }

}

class CitiesListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CitiesListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CitiesListViewModel(
                dataSource = CityDataSource.getDataSource(context.resources),
                cacheDir = context.cacheDir
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}