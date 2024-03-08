package com.bannanguy.task1androidapp.ui.cityDetail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bannanguy.task1androidapp.R
import com.bannanguy.task1androidapp.data.api.weather.RetrofitClient
import com.bannanguy.task1androidapp.data.api.weather.RetrofitClientFactory
import com.bannanguy.task1androidapp.databinding.CityDetailActivityBinding
import com.bannanguy.task1androidapp.databinding.CityListActivityBinding
import com.bannanguy.task1androidapp.ui.cityList.CitiesListActivity
import com.squareup.picasso.Picasso
import java.io.File
import kotlin.math.round

const val CITY_ID = "city id"

class CityDetailActivity : AppCompatActivity() {
    private lateinit var binding: CityDetailActivityBinding

    private val cityDetailViewModel by viewModels<CityDetailViewModel> {
        CityDetailViewModelFactory(this)
    }

    private var currentCityId: Long? = null
    private lateinit var retrofitWeatherClient: RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CityDetailActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        addOnBackPressedCallback()
        setOnClickListener()

        readCurrentCityId()
        observeData()

        initRetrofitClient(cacheDir)
        loadWeatherData()
    }

    private fun addOnBackPressedCallback() {
        val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@CityDetailActivity, CitiesListActivity()::class.java)
                startActivity(intent)
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

    }

    private fun observeData() {
        cityDetailViewModel.observeLiveData().observe(this) {
            it?.let {
                binding.cityName.text = it.name
                binding.cityTemp.text = String.format(
                    "%s %s",
                    it.temp_c.toString(),
                    resources.getString(R.string.celsius_symbol)
                )
                Picasso.get()
                    .load("https:" + it.condition_icon_uri)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder_error)
                    .into(binding.cityWeatherIcon)
                binding.cityConditionText.text = it.condition_text
                val windMps = round(it.wind_kph * 1000 / 3600 * 100) / 100
                binding.cityWindMps.text = windMps.toString()
                binding.cityWindDir.text = it.wind_dir
            }
        }

    }

    private fun setOnClickListener() {
        binding.backButton.setOnClickListener {
            val intent = Intent(this, CitiesListActivity()::class.java)
            startActivity(intent)
        }
    }

    private fun readCurrentCityId() {
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentCityId = bundle.getLong(CITY_ID)
        }
    }

    private fun initRetrofitClient(cacheDir: File) {
        retrofitWeatherClient = RetrofitClientFactory.createBySingleton(
            "weatherapi",
            cacheDir
        )
    }

    private fun loadWeatherData() {
        if (currentCityId != null) {
            cityDetailViewModel.loadWeatherDataOfCity(
                retrofitWeatherClient,
                currentCityId!!
            )
        } else {
            binding.cityName.text = resources.getString(R.string.city_name_unknown)
        }
    }

}