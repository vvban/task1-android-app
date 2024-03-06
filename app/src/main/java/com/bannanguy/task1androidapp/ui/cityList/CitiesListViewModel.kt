package com.bannanguy.task1androidapp.ui.cityList

import android.content.Context
import androidx.lifecycle.*
import com.bannanguy.task1androidapp.data.api.weather.WeatherAPI
import com.bannanguy.task1androidapp.data.*
import com.bannanguy.task1androidapp.data.api.weather.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CitiesListViewModel(
    private val dataSource: CityDataSource
) : ViewModel() {

    private val citiesWeatherInfoLiveData: MutableLiveData<List<CityWeatherInfo>> by lazy {
        MutableLiveData<List<CityWeatherInfo>>()
    }

    fun loadWeatherData(
        retrofitClient: RetrofitClient
    ) {
        val listOfCities = dataSource.getCityList()

        val currentListOfCityWeatherInfo: MutableList<CityWeatherInfo> =
            ArrayList<CityWeatherInfo>(0).toMutableList()

        listOfCities.forEach { city ->
            viewModelScope.launch(Dispatchers.IO) {

                WeatherAPI.getWeatherByCity(
                    retrofitClient,
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
                dataSource = CityDataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}