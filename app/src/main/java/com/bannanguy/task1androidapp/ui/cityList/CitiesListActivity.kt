package com.bannanguy.task1androidapp.ui.cityList

import android.annotation.SuppressLint
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

const val CITY_ID = "city id"

class CitiesListActivity : AppCompatActivity() {
    private val citiesListViewModel by viewModels<CitiesListViewModel> {
        CitiesListViewModelFactory(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.city_list_activity)

        /* We can merge several adapters to one */
        val citiesAdapter = CitiesAdapter(resources) {
            city -> adapterOnClick(city)
        }

        /* Set Adapter */
        val concatAdapter = ConcatAdapter(citiesAdapter)
        val recyclerView: RecyclerView = findViewById(R.id.city_weather_list_recycler_view)
        recyclerView.adapter = concatAdapter

        /* Observe */
        citiesAdapter.currentList.clear()

        citiesListViewModel.observeLiveData().observe(this) {
            it?.let {
                citiesAdapter.submitList(it as MutableList<CityWeatherInfo>)
                citiesAdapter.notifyDataSetChanged()
            }
        }

        /* Data request via API */
        citiesListViewModel.loadWeatherData()
    }

    /* Opens CityDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(cityWeatherInfo: CityWeatherInfo) {
        val intent = Intent(this, CityDetailActivity()::class.java)
        intent.putExtra(CITY_ID, cityWeatherInfo.city_id)
        startActivity(intent)
    }

}