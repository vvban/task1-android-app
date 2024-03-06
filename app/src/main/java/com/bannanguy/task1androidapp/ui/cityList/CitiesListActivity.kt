package com.bannanguy.task1androidapp.ui.cityList

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bannanguy.task1androidapp.R
import com.bannanguy.task1androidapp.data.CityWeatherInfo
import com.bannanguy.task1androidapp.data.api.weather.RetrofitClient
import com.bannanguy.task1androidapp.data.api.weather.RetrofitClientFactory
import com.bannanguy.task1androidapp.ui.cityDetail.CITY_ID
import com.bannanguy.task1androidapp.ui.cityDetail.CityDetailActivity
import java.io.File

class CitiesListActivity : AppCompatActivity() {
    private val citiesListViewModel by viewModels<CitiesListViewModel> {
        CitiesListViewModelFactory(this)
    }

    private lateinit var citiesAdapter: CitiesAdapter
    private lateinit var retrofitWeatherClient: RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.city_list_activity)

        initAdapter()
        setAdapter()

        observeData()

        initRetrofitClient(cacheDir)
        loadWeatherData()
    }

    private fun initAdapter() {

        fun adapterOnClick(cityWeatherInfo: CityWeatherInfo) {
            /* Opens CityDetailActivity when RecyclerView item is clicked. */
            val intent = Intent(this, CityDetailActivity()::class.java)
            intent.putExtra(CITY_ID, cityWeatherInfo.city_id)
            finish() // Easy way to clear previous data
            startActivity(intent)
        }

        citiesAdapter = CitiesAdapter(resources) {
            city -> adapterOnClick(city)
        }
    }

    private fun setAdapter() {
        val concatAdapter = ConcatAdapter(citiesAdapter)
        val recyclerView: RecyclerView = findViewById(R.id.city_weather_list_recycler_view)
        recyclerView.adapter = concatAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData() {
        // FIXME: We need it?
//        citiesAdapter.currentList.clear()

        citiesListViewModel.observeLiveData().observe(this) {
            it?.let {
                citiesAdapter.submitList(it as MutableList<CityWeatherInfo>)
                // FIXME: We can't known when the last item will be added
                citiesAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initRetrofitClient(cacheDir: File) {
        retrofitWeatherClient = RetrofitClientFactory.createBySingleton(
            "weatherapi",
            cacheDir
        )
    }

    private fun loadWeatherData() {
        citiesListViewModel.loadWeatherData(retrofitWeatherClient)
    }

}