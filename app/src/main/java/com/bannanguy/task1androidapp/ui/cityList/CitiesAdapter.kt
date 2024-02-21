package com.bannanguy.task1androidapp.ui.cityList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bannanguy.task1androidapp.R
import com.bannanguy.task1androidapp.data.CityWeatherInfo
import com.bannanguy.task1androidapp.data.getListOfCities

class CitiesAdapter(private val onClick: (CityWeatherInfo) -> Unit) :
    ListAdapter<CityWeatherInfo, CitiesAdapter.CityWeatherInfoViewHolder>(CityWeatherInfoDiffCallback) {

    fun notifyItemInsertedAtLastPosition (lastPosition: Int) {
        notifyItemInserted(lastPosition)
    }

    /* ViewHolder for CityWeatherInfo, takes in the inflated view and the onClick behavior. */
    class CityWeatherInfoViewHolder(itemView: View, val onClick: (CityWeatherInfo) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val cityNameTextView: TextView = itemView.findViewById(R.id.city_name)
        private val cityTempTextView: TextView = itemView.findViewById(R.id.city_temp)
        private var currentCityWeatherInfo: CityWeatherInfo? = null

        init {
            itemView.setOnClickListener {
                currentCityWeatherInfo?.let {
                    onClick(it)
                }
            }
        }

        /* Bind city name and temperature. */
        fun bind(cityWeatherInfo: CityWeatherInfo) {
            currentCityWeatherInfo = cityWeatherInfo

            cityNameTextView.text = cityWeatherInfo.name // FIXME: get name from getListOfCities()
            cityTempTextView.text = cityWeatherInfo.temp.toString()
        }
    }

    /* Creates and inflates view and return CityWeatherInfoViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityWeatherInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_weater_item, parent, false)
        return CityWeatherInfoViewHolder(view, onClick)
    }

    /* Gets current CityWeatherInfo and uses it to bind view. */
    override fun onBindViewHolder(holder: CityWeatherInfoViewHolder, position: Int) {
        if (itemCount - 1 < position) return // FIXME: ?
        val cityWeatherInfo = getItem(position)
        holder.bind(cityWeatherInfo)
    }
}

object CityWeatherInfoDiffCallback : DiffUtil.ItemCallback<CityWeatherInfo>() {
    override fun areItemsTheSame(oldItem: CityWeatherInfo, newItem: CityWeatherInfo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CityWeatherInfo, newItem: CityWeatherInfo): Boolean {
        return oldItem.city_id == newItem.city_id
    }
}