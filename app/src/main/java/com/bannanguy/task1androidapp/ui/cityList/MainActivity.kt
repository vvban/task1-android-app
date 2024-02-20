package com.bannanguy.task1androidapp.ui.cityList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bannanguy.task1androidapp.R
import com.bannanguy.task1androidapp.api.WeatherAdapter
import com.bannanguy.task1androidapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var  weatherAdapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        // Create the observer which updates the UI.
//        val nameObserver = Observer<String> { newName ->
//            // Update the UI, in this case, a TextView.
//            nameTextView.text = newName
//        }
//
//        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
//        model.currentName.observe(this, nameObserver)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        weatherAdapter = WeatherAdapter()
        viewModel.getWeather()
        viewModel.observeLiveData().observe(this) {
            weatherAdapter.setData(it)
        }

        val recyclerView: RecyclerView = findViewById(R.id.city_recycler_view)
        recyclerView.adapter = weatherAdapter


//        weatherAdapter.setOnCityClickListener(object  : WeatherAdapter.SetOnCityClickListener{
//            override fun setOnClickListener() {
//                Log.i("MainActivity", "click")
////                val intent = Intent(applicationContext, DetailsActivity::class.java)
////                intent.putExtra(MEAL_ID, mealsByCategoryName.idMeal)
////                intent.putExtra(MEAL_Name, mealsByCategoryName.strMeal)
////                intent.putExtra(MEAL_THUMB, mealsByCategoryName.strMealThumb)
////                startActivity(intent)
//            }
//        })

    }

//    private fun prepareRecyclerView() {
//        weatherAdapter = WeatherAdapter()
////        binding.recyclerView.apply {
////            layoutManager = GridLayoutManager(applicationContext ,2)
////            adapter = weatherAdapter
////        }
//    }
}
