package com.bannanguy.task1androidapp.data

import android.content.res.Resources

/* Handles operations on citiesList and holds details about. */
class CityDataSource(resources: Resources) {
    private val citiesList = getListOfCities(resources)

    /* Returns city given an ID. */
    fun getCityForId(id: Long): CityData? {
        citiesList.let { cities ->
            return cities.firstOrNull{ it.id == id}
        }
    }

    fun getCityList(): List<CityData> {
        return citiesList
    }

    companion object {
        private var INSTANCE: CityDataSource? = null

        fun getDataSource(resources: Resources): CityDataSource {
            return synchronized(CityDataSource::class) {
                val newInstance = INSTANCE ?: CityDataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}