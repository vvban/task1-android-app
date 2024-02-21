package com.bannanguy.task1androidapp.ui.cityDetail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bannanguy.task1androidapp.R
import com.bannanguy.task1androidapp.data.CityDetailWeatherInfo
import com.bannanguy.task1androidapp.ui.cityList.CitiesListActivity
import com.squareup.picasso.Picasso

class CityDetailActivity : AppCompatActivity() {

    private val cityDetailViewModel by viewModels<CityDetailViewModel> {
        CityDetailViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.city_detail_activity)

        var currentFlowerId: Long? = null

        /* Connect variables to UI elements. */
        val cityNameTextView: TextView = this.findViewById(R.id.city_name)
        val cityTempTextView: TextView = this.findViewById(R.id.city_temp)
        val cityWeatherIconImageView: ImageView = this.findViewById(R.id.city_weather_icon)
        val cityConditionTextView: TextView = this.findViewById(R.id.city_condition_text)
        val cityWindMphTextView: TextView = this.findViewById(R.id.city_wind_mph)
        val cityWindKphTextView: TextView = this.findViewById(R.id.city_wind_kph)
        val cityWindDirTextView: TextView = this.findViewById(R.id.city_wind_dir)
        val backButton: Button = findViewById(R.id.back_button)

//        val bundle: Bundle? = intent.extras
//        if (bundle != null) {
//            currentFlowerId = bundle.getLong(FLOWER_ID)
//        }

        cityDetailViewModel.observeLiveData().observe(this) {
            it?.let {
//                citiesAdapter.submitList(it as MutableList<CityWeatherInfo>)
                cityNameTextView.text = it.name
                cityTempTextView.text = it.temp_c.toString()
                Picasso.get()
                    .load("https:" + it.condition_icon_uri)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder_error)
                    .into(cityWeatherIconImageView)
                cityConditionTextView.text = it.condition_text
                cityWindMphTextView.text = it.wind_mph.toString()
                cityWindKphTextView.text = it.wind_kph.toString()
                cityWindDirTextView.text = it.wind_dir
            }
        }

        // TODO: temp
        cityDetailViewModel.updateLiveData(
            CityDetailWeatherInfo(
                1,
                "Київ",
                23.3444,
                "//cdn.weatherapi.com/weather/64x64/night/176.png",
                "Все добре",
                34.344,
                32.3443,
                "Вітер схід"
            )
        )

        backButton.setOnClickListener {
            val intent = Intent(this, CitiesListActivity()::class.java)
            startActivity(intent)
        }

    }
}