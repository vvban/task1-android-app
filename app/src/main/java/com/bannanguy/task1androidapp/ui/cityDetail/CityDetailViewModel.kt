package com.bannanguy.task1androidapp.ui.cityDetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bannanguy.task1androidapp.data.CityDetailWeatherInfo

class CityDetailViewModel() : ViewModel() {

    private val cityDetailInfoLiveData: MutableLiveData<CityDetailWeatherInfo> by lazy {
        MutableLiveData<CityDetailWeatherInfo>()
    }

    fun updateLiveData(data: CityDetailWeatherInfo) {
        cityDetailInfoLiveData.postValue(data)
    }

    fun observeLiveData() : LiveData<CityDetailWeatherInfo> {
        return cityDetailInfoLiveData
    }
}

class CityDetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CityDetailViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}