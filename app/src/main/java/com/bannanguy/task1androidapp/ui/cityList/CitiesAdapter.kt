package com.bannanguy.task1androidapp.ui.cityList

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bannanguy.task1androidapp.R
import com.bannanguy.task1androidapp.data.CityWeatherInfo

class CitiesAdapter(
    private val dataSet: ArrayList<CityWeatherInfo>,
    private val resources: Resources,
    private val owner: LifecycleOwner,
    private val onClick: (CityWeatherInfo) -> Unit
) : RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {

    class ViewHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {

        val cityNameTextView: TextView
        val cityTempTextView: TextView

        init {
            cityNameTextView = view.findViewById(R.id.city_name)
            cityTempTextView = view.findViewById(R.id.city_temp)
        }

    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.city_weater_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val item = dataSet[position]

        viewHolder.itemView.setOnClickListener {
            onClick(item)
        }

        viewHolder.cityNameTextView.text = item.name

        item.temp.observe(owner) {
            val temp = item.temp.value
            if (temp?.isNaN() == false) {
                viewHolder.cityTempTextView.text = String.format(
                    "%s %s",
                    temp.toString(),
                    resources.getString(R.string.celsius_symbol)
                )
            }
        }

    }

    override fun getItemCount() = dataSet.size

    fun setList(newList: List<CityWeatherInfo>) {
        DiffUtil.calculateDiff(UserDiffUtil(this.dataSet, newList)).dispatchUpdatesTo(this)
        this.dataSet.clear()
        this.dataSet.addAll(newList)
    }

}

class UserDiffUtil(
    private val oldList: List<CityWeatherInfo>,
    private val newList: List<CityWeatherInfo>
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