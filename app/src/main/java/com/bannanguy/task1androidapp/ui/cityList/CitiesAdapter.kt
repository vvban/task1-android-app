package com.bannanguy.task1androidapp.ui.cityList

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bannanguy.task1androidapp.R
import com.bannanguy.task1androidapp.data.CityWeatherInfo
import com.bannanguy.task1androidapp.databinding.CityWeaterItemBinding

class CitiesAdapter(
    private val dataSet: ArrayList<CityWeatherInfo>,
    private val resources: Resources,
    private val owner: LifecycleOwner,
    private val onClick: (CityWeatherInfo) -> Unit
) : RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: CityWeaterItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            viewHolder: ViewHolder,
            owner: LifecycleOwner,
            resources: Resources,
            onClick: (CityWeatherInfo) -> Unit,
            cityWeatherInfo: CityWeatherInfo
        ) {
            binding.cityName.text = cityWeatherInfo.name

            cityWeatherInfo.temp.observe(owner) {
                val temp = cityWeatherInfo.temp.value
                if (temp?.isNaN() == false) {
                    binding.cityTemp.text = String.format(
                        "%s %s",
                        temp.toString(),
                        resources.getString(R.string.celsius_symbol)
                    )
                }
            }

            viewHolder.itemView.setOnClickListener {
                onClick(cityWeatherInfo)
            }

        }

    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = CityWeaterItemBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]
        viewHolder.bind(viewHolder, owner, resources, onClick, item)
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