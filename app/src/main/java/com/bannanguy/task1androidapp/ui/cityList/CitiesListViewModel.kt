package com.bannanguy.task1androidapp.ui.cityList

import android.content.Context
import androidx.lifecycle.*
import com.bannanguy.task1androidapp.data.api.weather.WeatherAPI
import com.bannanguy.task1androidapp.data.*
import com.bannanguy.task1androidapp.data.api.weather.RetrofitClient
import com.bannanguy.task1androidapp.data.source.CityData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CitiesListViewModel : ViewModel() {

    private val citiesWeatherInfoLiveData: MutableLiveData<List<CityWeatherInfo>> by lazy {
        MutableLiveData<List<CityWeatherInfo>>()
    }

    private val currentListOfCityWeatherInfo: MutableList<CityWeatherInfo> =
        ArrayList<CityWeatherInfo>(0).toMutableList()

    fun addCitiesToList(
        retrofitClient: RetrofitClient,
        listOfCities: List<CityData>
    ) {
        listOfCities.forEach { city ->
            val cityWeatherInfo = CityWeatherInfo(
                city.id,
                city.name,
                MutableLiveData<Double>(Double.NaN)
            )

            fetchCityWeatherInfo(
                retrofitClient,
                city,
                cityWeatherInfo
            )

            currentListOfCityWeatherInfo.add(cityWeatherInfo)
        }

        // Send all
        citiesWeatherInfoLiveData.postValue(
            currentListOfCityWeatherInfo
        )

    }

    private fun fetchCityWeatherInfo(
        retrofitClient: RetrofitClient,
        city: CityData,
        cityWeatherInfo: CityWeatherInfo
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            WeatherAPI.getWeatherByCity(
                retrofitClient,
                city
            ) { weatherResponse ->
                cityWeatherInfo.temp.postValue(
                    weatherResponse.current.temp_c
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
            return CitiesListViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}