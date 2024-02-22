package com.bannanguy.task1androidapp.ui.cityList

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bannanguy.task1androidapp.api.weather.WeatherInstance
import com.bannanguy.task1androidapp.data.*
import com.bannanguy.task1androidapp.utils.ConfigPropertiesUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CitiesListViewModel(private val dataSource: CityDataSource) : ViewModel() {

    // FIXME: Try just livedata with mutablelist
    // FIXME: view how work DataSource at flowers
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
            WeatherInstance.api.fetchSingleCityWeather(
                apiKey,
                city.lat.toString() + "," + city.lon.toString()
            ).enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    response.body()?.let { weatherResponse ->

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

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.e("CitiesListViewModel", t.message.toString())
                }

            })
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