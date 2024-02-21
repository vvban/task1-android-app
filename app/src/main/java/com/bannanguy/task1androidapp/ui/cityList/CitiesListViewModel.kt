package com.bannanguy.task1androidapp.ui.cityList

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bannanguy.task1androidapp.api.weather.WeatherInstance
import com.bannanguy.task1androidapp.data.CityData
import com.bannanguy.task1androidapp.data.CityWeatherInfo
import com.bannanguy.task1androidapp.data.WeatherResponse
import com.bannanguy.task1androidapp.data.getListOfCities
import com.bannanguy.task1androidapp.utils.ConfigPropertiesUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CitiesListViewModel() : ViewModel() {

    private val citiesWeatherInfoLiveData: MutableLiveData<List<CityWeatherInfo>> by lazy {
        MutableLiveData<List<CityWeatherInfo>>()
    }

    fun loadWeatherData(resources: Resources) {

        val apiKey = ConfigPropertiesUtils.getValue("api")

        if (apiKey == null) {
            println("Error: API key is null.")
            return
        }

        // FIXME: In memory list is not allocated and isn't fixed
        //  -> recreating each time when one item will be inserted
        val listOfCities: List<CityData> = getListOfCities(resources)
        var currentLiveData: MutableList<CityWeatherInfo> =
            citiesWeatherInfoLiveData.value?.toMutableList() ?:
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

                        currentLiveData.add(cityWeatherInfo)

                        citiesWeatherInfoLiveData.postValue(
                            currentLiveData
                        )

//                        citiesWeatherInfoLiveData.postValue(citiesInfoList)
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.e("MainViewModel", t.message.toString())
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
            return CitiesListViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}