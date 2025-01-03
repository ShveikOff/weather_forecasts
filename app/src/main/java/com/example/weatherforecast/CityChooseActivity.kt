package com.example.weatherforecast

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.api.APImanager
import com.example.weatherforecast.databinding.ActivityCityChooseBinding

class CityChooseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCityChooseBinding
    private lateinit var cityAdapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        APImanager.initialize(this)

        // Настройка RecyclerView
        cityAdapter = CityAdapter(emptyList()) { selectedCity ->
            // Обработка клика по элементу города
            handleCityClick(selectedCity)
        }
        binding.cityRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.cityRecyclerView.adapter = cityAdapter


        // Обработчик поиска
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    performCitySearch(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    performCitySearch(it)
                }
                return true
            }
        })
    }

    private fun performCitySearch(query: String) {
        Log.d("CitySearch", "Starting search for query: $query") // Лог начала поиска

        APImanager.searchCities(query) { cities ->
            runOnUiThread {
                if (cities != null) {
                    Log.d("CitySearch", "Cities found: ${cities.size}") // Лог количества найденных городов

                    // Выполняем запрос для обновления AQI для каждого города
                    cities.forEach { city ->
                        Log.d("CitySearch", "Fetching AQI for city: ${city.name} (${city.lat}, ${city.lon})") // Лог информации о городе

                        APImanager.getAirQuality(city.lat, city.lon) { aqiResponse ->
                            if (aqiResponse != null) {
                                Log.d("CitySearch", "AQI response received for city: ${city.name}") // Лог успешного получения AQI

                                // Обновляем AQI для города
                                city.aqi = when (aqiResponse.list.firstOrNull()?.main?.aqi) {
                                    1 -> "Good"
                                    2 -> "Fair"
                                    3 -> "Moderate"
                                    4 -> "Poor"
                                    5 -> "Very Poor"
                                    else -> "N/A"
                                }
                                Log.d("CitySearch", "Updated AQI for city: ${city.name}, AQI: ${city.aqi}") // Лог обновления AQI

                                // Уведомляем адаптер об изменении данных
                                runOnUiThread {
                                    cityAdapter.notifyDataSetChanged()
                                }
                            } else {
                                Log.e("CitySearch", "Failed to fetch AQI for city: ${city.name}") // Лог ошибки получения AQI
                            }
                        }
                    }
                    // Обновляем адаптер с базовыми данными (без AQI)
                    cityAdapter.updateCities(cities)
                } else {
                    Log.e("CitySearch", "Failed to load cities") // Лог ошибки загрузки городов
                }
            }
        }
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
