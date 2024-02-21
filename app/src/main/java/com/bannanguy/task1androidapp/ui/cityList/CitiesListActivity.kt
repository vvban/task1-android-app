package com.bannanguy.task1androidapp.ui.cityList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bannanguy.task1androidapp.R
import com.bannanguy.task1androidapp.data.CityDataSource
import com.bannanguy.task1androidapp.data.CityWeatherInfo
import com.bannanguy.task1androidapp.ui.cityDetail.CityDetailActivity

class CitiesListActivity : AppCompatActivity() {
    private val citiesListViewModel by viewModels<CitiesListViewModel> {
        CitiesListViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.city_list_activity)

        /* We can merge several adapters to one */
        val citiesAdapter = CitiesAdapter(resources) {
            city -> adapterOnClick(city)
        }
        val concatAdapter = ConcatAdapter(citiesAdapter)

        /* Set Adapter */
        val recyclerView: RecyclerView = findViewById(R.id.city_weather_list_recycler_view)
        recyclerView.adapter = concatAdapter

        /* Observe */
        citiesListViewModel.observeLiveData().observe(this) {
            it?.let {
                citiesAdapter.submitList(it)
                // FIXME: It may be optimized
                citiesAdapter.notifyDataSetChanged()
            }
        }

        /* Data request via API */
        citiesListViewModel.loadWeatherData()

    }

    /* Opens CityDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(cityWeatherInfo: CityWeatherInfo) {
        val intent = Intent(this, CityDetailActivity()::class.java)
//        intent.putExtra(CITY_ID, cityWeatherInfo.id)
        startActivity(intent)
    }
}