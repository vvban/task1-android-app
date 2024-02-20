package com.bannanguy.task1androidapp.api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bannanguy.task1androidapp.R
import com.bannanguy.task1androidapp.data.CityInfo
//import com.bannanguy.task1androidapp.databinding.WeatherLayoutBinding


class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    private var listOfCityInfo = ArrayList<CityInfo>()
//    private lateinit var setOnCardClickListener : SetOnCardClickListener
//
//    interface SetOnCardClickListener {
//        fun setOnClickListener()
//    }

    fun setData(listOfCityInfo : List<CityInfo>) {
        this.listOfCityInfo = listOfCityInfo as ArrayList<CityInfo>
        notifyItemRangeInserted(0, listOfCityInfo.size)
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cityNameTextView: TextView

        init {
            // Define click listener for the ViewHolder's View
            cityNameTextView = view.findViewById(R.id.city_name)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.city_card_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.cityNameTextView.text = listOfCityInfo[position].name
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = listOfCityInfo.size

}

//
//class Weather2Adapter : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
//
//    private var listOfCityInfo = ArrayList<CityInfo>()
//    private lateinit var setOnCityClickListener : SetOnCityClickListener
//
//    fun setOnCityClickListener(setOnCityClickListener: SetOnCityClickListener){
//        this.setOnCityClickListener = setOnCityClickListener
//    }
//    fun setData(listOfCityInfo : List<CityInfo>) {
//        this.listOfCityInfo = listOfCityInfo as ArrayList<CityInfo>
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(val  binding: WeatherLayoutBinding)  : RecyclerView.ViewHolder(binding.root){}
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            WeatherLayoutBinding.inflate(
//                LayoutInflater.from(
//                    parent.context
//                )
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int)
//    {
////        Glide.with(holder.itemView).load(listOfMeals[position].strMealThumb).into(holder.binding.recipeImage)
//        holder.binding.cityName.text= listOfCityInfo[position].name
//    }
//
//    override fun getItemCount(): Int {
//        return listOfCityInfo.size
//    }
//    interface SetOnCityClickListener {
//        fun setOnClickListener()
//    }
//
//}