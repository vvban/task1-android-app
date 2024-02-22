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
import com.bannanguy.task1androidapp.ui.cityList.CITY_ID
import com.bannanguy.task1androidapp.ui.cityList.CitiesListActivity
import com.squareup.picasso.Picasso
import kotlin.math.round


class CityDetailActivity : AppCompatActivity() {

    private val cityDetailViewModel by viewModels<CityDetailViewModel> {
        CityDetailViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.city_detail_activity)

        val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@CityDetailActivity, CitiesListActivity()::class.java)
                startActivity(intent)
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        var currentCityId: Long? = null

        /* Connect variables to UI elements. */
        val cityNameTextView: TextView = this.findViewById(R.id.city_name)
        val cityTempTextView: TextView = this.findViewById(R.id.city_temp)
        val cityWeatherIconImageView: ImageView = this.findViewById(R.id.city_weather_icon)
        val cityConditionTextView: TextView = this.findViewById(R.id.city_condition_text)
        val cityWindKphTextView: TextView = this.findViewById(R.id.city_wind_mps)
        val cityWindDirTextView: TextView = this.findViewById(R.id.city_wind_dir)
        val backButton: Button = findViewById(R.id.back_button)

        cityDetailViewModel.observeLiveData().observe(this) {
            it?.let {
                cityNameTextView.text = it.name
                cityTempTextView.text = String.format(
                    "%s %s",
                    it.temp_c.toString(),
                    resources.getString(R.string.celsius_symbol)
                )
                Picasso.get()
                    .load("https:" + it.condition_icon_uri)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder_error)
                    .into(cityWeatherIconImageView)
                cityConditionTextView.text = it.condition_text
                val windMps = round(it.wind_kph * 1000 / 3600 * 100) / 100
                cityWindKphTextView.text = windMps.toString()
                cityWindDirTextView.text = it.wind_dir
            }
        }

        backButton.setOnClickListener {
            val intent = Intent(this, CitiesListActivity()::class.java)
            startActivity(intent)
        }

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentCityId = bundle.getLong(CITY_ID)
        }

        if (currentCityId != null) {
            cityDetailViewModel.loadWeatherDataOfCity(
                currentCityId
            )
        } else {
            cityNameTextView.text = resources.getString(R.string.city_name_unknown)
        }

    }

}