package com.bannanguy.task1androidapp.ui.cityList

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bannanguy.task1androidapp.R
import com.bannanguy.task1androidapp.data.CityDataSource
import com.bannanguy.task1androidapp.data.CityWeatherInfo

class CitiesAdapter(
    private val dataSet: ArrayList<CityWeatherInfo>,
    private val resources: Resources,
    private val onClick: (CityWeatherInfo) -> Unit
) :
    RecyclerView.Adapter<CitiesAdapter.ViewHolder>(){

    /* ViewHolder for CityWeatherInfo, takes in the inflated view and the onClick behavior. */
    class ViewHolder(
        view: View,
        val resources: Resources,
        onClick: (CityWeatherInfo) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        val cityNameTextView: TextView
        val cityTempTextView: TextView

        init {
            cityNameTextView = view.findViewById(R.id.city_name)
            cityTempTextView = view.findViewById(R.id.city_temp)

            view.setOnClickListener {
                currentCityWeatherInfo?.let {
                    onClick(it)
                }
            }
        }

    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.city_weater_item, viewGroup, false)
        return ViewHolder(view, resources, onClick)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val item = dataSet[position]
        viewHolder.currentCityWeatherInfo = item

        val cityData = CityDataSource.getDataSource(resources)
            .getCityForId(item.city_id)
        viewHolder.cityNameTextView.text = cityData?.name ?: resources.getString(R.string.city_name_unknown)
        viewHolder.cityTempTextView.text = String.format(
            "%s %s",
            item.temp.toString(),
            resources.getString(R.string.celsius_symbol)
        )
    }

    override fun getItemCount() = dataSet.size

    fun setList(newList: ArrayList<CityWeatherInfo>) {
        DiffUtil.calculateDiff(UserDiffUtil(this.dataSet, newList)).dispatchUpdatesTo(this)
        this.dataSet.clear()
        this.dataSet.addAll(newList)
    }
}

//object CityWeatherInfoDiffCallback : DiffUtil.ItemCallback<CityWeatherInfo>() {
//    override fun areItemsTheSame(oldItem: CityWeatherInfo, newItem: CityWeatherInfo): Boolean {
//        return oldItem.city_id == newItem.city_id
//    }
//
//    override fun areContentsTheSame(oldItem: CityWeatherInfo, newItem: CityWeatherInfo): Boolean {
//        return oldItem.city_id == newItem.city_id
//    }
//}

class UserDiffUtil(
    val oldList: List<CityWeatherInfo>,
    val newList: List<CityWeatherInfo>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}