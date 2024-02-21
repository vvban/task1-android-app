package com.bannanguy.task1androidapp.ui.cityList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bannanguy.task1androidapp.R
import com.bannanguy.task1androidapp.data.CityWeatherInfo
import com.bannanguy.task1androidapp.ui.cityDetail.CityDetailActivity

class CitiesListActivity : AppCompatActivity() {
    private val citiesListViewModel by viewModels<CitiesListViewModel> {
        CitiesListViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* We can several adapters merge to concatAdapter. */
        val citiesAdapter = CitiesAdapter { city -> adapterOnClick(city) }
        val concatAdapter = ConcatAdapter(citiesAdapter)

        val recyclerView: RecyclerView = findViewById(R.id.city_weather_list_recycler_view)
        recyclerView.adapter = concatAdapter

        citiesListViewModel.observeLiveData().observe(this) {
            it?.let {
                // Because we load weather data by one request for each location
                // Only notify that LiveData was changed
                citiesAdapter.notifyItemInsertedAtLastPosition(it.size - 1)
                // FIXME: Need we submit List or only notify?
                citiesAdapter.submitList(it as MutableList<CityWeatherInfo>)
            }
        }

        citiesListViewModel.loadWeatherData(resources)

    }

    /* Opens CityDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(cityWeatherInfo: CityWeatherInfo) {
        Log.d("Event", "Click")
        val intent = Intent(this, CityDetailActivity()::class.java)
//        intent.putExtra(CITY_ID, cityWeatherInfo.id)
        startActivity(intent)
    }
}