package com.example.weatherforecast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.databinding.ActivityCityChooseBinding

class CityChooseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCityChooseBinding
    private lateinit var cityAdapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Пример списка городов
        val cities = listOf(
            City("Bishkek", "29", "12°/5°", "12"),
            City("Osh", "44", "16°/9°", "16"),
            City("Almaty", "55", "9°/3°", "9")
        )

        // Настройка RecyclerView
        cityAdapter = CityAdapter(cities) { selectedCity ->
            // Обработка клика по элементу города
            handleCityClick(selectedCity)
        }
        binding.cityRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.cityRecyclerView.adapter = cityAdapter

        // Обработчик поиска
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    filterCities(it, cities)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    filterCities(it, cities)
                }
                return true
            }
        })
    }

    private fun filterCities(query: String, cities: List<City>) {
        val filteredCities = cities.filter { it.name.contains(query, ignoreCase = true) }
        cityAdapter.updateCities(filteredCities)
    }

    private fun handleCityClick(city: City) {
        // Логика обработки выбора города
        // Например, передать данные обратно в другое активити или открыть новую страницу
        println("Selected city: ${city.name}")
    }
}

// Класс данных для города
data class City(
    val name: String,
    val aqi: String,
    val details: String,
    val temperature: String
)
