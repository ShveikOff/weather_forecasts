package com.example.weatherforecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FavoriteCitiesAdapter(
    private val cities: List<City>,
    private val onCityClick: (City) -> Unit
) : RecyclerView.Adapter<FavoriteCitiesAdapter.FavoriteCityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return FavoriteCityViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteCityViewHolder, position: Int) {
        val city = cities[position]
        holder.bind(city)
        holder.itemView.setOnClickListener {
            onCityClick(city)
        }
    }

    override fun getItemCount(): Int = cities.size

    class FavoriteCityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(city: City) {
            itemView.findViewById<TextView>(R.id.cityName).text = city.name
            itemView.findViewById<TextView>(R.id.cityDetails).text = city.details
            itemView.findViewById<TextView>(R.id.cityTemperature).text = city.temperature
        }
    }
}


