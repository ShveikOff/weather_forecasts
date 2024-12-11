package com.example.weatherforecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CityAdapter(
    private var cities: List<City>,
    private val onCityClick: (City) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_card, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities[position]
        holder.bind(city, onCityClick)
    }

    override fun getItemCount(): Int = cities.size

    fun updateCities(newCities: List<City>) {
        cities = newCities
        notifyDataSetChanged()
    }

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cityName: TextView = itemView.findViewById(R.id.cityName)
        private val cityDetails: TextView = itemView.findViewById(R.id.cityDetails)
        private val cityTemp: TextView = itemView.findViewById(R.id.cityTemp)

        fun bind(city: City, onCityClick: (City) -> Unit) {
            cityName.text = city.name
            cityDetails.text = "AQI ${city.aqi} ${city.details}"
            cityTemp.text = city.temperature
            itemView.setOnClickListener { onCityClick(city) }
        }
    }
}
