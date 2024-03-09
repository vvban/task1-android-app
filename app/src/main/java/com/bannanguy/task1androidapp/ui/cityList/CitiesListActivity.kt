package com.bannanguy.task1androidapp.ui.cityList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bannanguy.task1androidapp.R
import com.bannanguy.task1androidapp.data.CityDataSource
import com.bannanguy.task1androidapp.data.CityWeatherInfo
import com.bannanguy.task1androidapp.data.api.weather.RetrofitClient
import com.bannanguy.task1androidapp.data.api.weather.RetrofitClientFactory
import com.bannanguy.task1androidapp.databinding.CityListActivityBinding
import com.bannanguy.task1androidapp.ui.cityDetail.CITY_ID
import com.bannanguy.task1androidapp.ui.cityDetail.CityDetailActivity
import java.io.File

class CitiesListActivity : AppCompatActivity() {
    private lateinit var binding: CityListActivityBinding

    private val citiesListViewModel by viewModels<CitiesListViewModel> {
        CitiesListViewModelFactory(this)
    }

    private lateinit var citiesAdapter: CitiesAdapter
    private lateinit var retrofitWeatherClient: RetrofitClient

    private var numberOfItem = 0 // init number
    private val itemToLoad = 12 // how many load per one time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CityListActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initAdapter()
        setAdapter()

//        citiesListViewModel.clearCitiesList()
        observeData()
        initRetrofitClient(cacheDir)
        loadCitiesIntoListWithPagination()

        setLoadMoreClickListener()
    }

    private fun initAdapter() {

        fun adapterOnClick(cityWeatherInfo: CityWeatherInfo) {
            /* Opens CityDetailActivity when RecyclerView item is clicked. */
            val intent = Intent(this, CityDetailActivity()::class.java)
            intent.putExtra(CITY_ID, cityWeatherInfo.city_id)
            finish() // Easy way to clear previous data
            startActivity(intent)
        }

        citiesAdapter = CitiesAdapter(
            ArrayList(0),
            resources,
            this
        ) {
            city -> adapterOnClick(city)
        }
    }

    private fun setAdapter() {
        val concatAdapter = ConcatAdapter(citiesAdapter)
        val recyclerView: RecyclerView = binding.cityWeatherListRecyclerView
        recyclerView.adapter = concatAdapter
    }

    private fun observeData() {

        citiesListViewModel.observeLiveData().observe(this) {
            it?.let {
                /** Show city item without temperature in RecycleView **/
                citiesAdapter.setList(it)
            }
        }
    }

    private fun initRetrofitClient(cacheDir: File) {
        retrofitWeatherClient = RetrofitClientFactory.createBySingleton(
            "weatherapi",
            cacheDir
        )
    }

    private fun loadCitiesIntoListWithPagination() {
        val listOfCities = CityDataSource(resources).getCityList()

        if (numberOfItem == listOfCities.size) {
            Toast.makeText(this, resources.getString(R.string.all_cities_are_loaded_message), Toast.LENGTH_SHORT).show();
            return
        }

        var newNumberOfItem = numberOfItem + itemToLoad
        if (newNumberOfItem > listOfCities.size) {
            newNumberOfItem = listOfCities.size
        }

        /**
         * Update life data with city weather info and Double.NaN temperature
         * Fetch real cities temperatures through API
         **/
        citiesListViewModel.addCitiesToList(
            retrofitWeatherClient,
            listOfCities.subList(
                numberOfItem,
                newNumberOfItem
            )
        )

        numberOfItem = newNumberOfItem
    }

    private fun setLoadMoreClickListener() {
        binding.loadMoreButton.setOnClickListener {
            loadCitiesIntoListWithPagination()
        }
    }

}
