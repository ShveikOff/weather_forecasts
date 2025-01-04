package com.example.weatherforecast

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.api.APImanager
import com.example.weatherforecast.databinding.ActivityCityChooseBinding

class CityChooseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCityChooseBinding
    private lateinit var cityAdapter: CityAdapter
    private lateinit var favoriteCityAdapter: FavoriteCitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        APImanager.initialize(this)

        // Настройка адаптера для списка избранных городов
        favoriteCityAdapter = FavoriteCitiesAdapter(FavoriteCitiesRepository.favoriteCities) { city ->
            showFavoriteCityDialog(city)
        }
        binding.favoriteCitiesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.favoriteCitiesRecyclerView.adapter = favoriteCityAdapter

        // Настройка RecyclerView для поиска городов
        cityAdapter = CityAdapter(emptyList()) { selectedCity ->
            showAddToFavoritesDialog(selectedCity)
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
        Log.d("CitySearch", "Starting search for query: $query")

        APImanager.searchCities(query) { cities ->
            runOnUiThread {
                if (cities != null) {
                    Log.d("CitySearch", "Cities found: ${cities.size}")

                    cities.forEach { city ->
                        Log.d("CitySearch", "Fetching AQI for city: ${city.name} (${city.lat}, ${city.lon})")

                        APImanager.getAirQuality(city.lat, city.lon) { aqiResponse ->
                            if (aqiResponse != null) {
                                Log.d("CitySearch", "AQI response received for city: ${city.name}")

                                city.aqi = when (aqiResponse.list.firstOrNull()?.main?.aqi) {
                                    1 -> "Good"
                                    2 -> "Fair"
                                    3 -> "Moderate"
                                    4 -> "Poor"
                                    5 -> "Very Poor"
                                    else -> "N/A"
                                }
                                Log.d("CitySearch", "Updated AQI for city: ${city.name}, AQI: ${city.aqi}")

                                runOnUiThread {
                                    cityAdapter.notifyDataSetChanged()
                                }
                            } else {
                                Log.e("CitySearch", "Failed to fetch AQI for city: ${city.name}")
                            }
                        }
                    }
                    cityAdapter.updateCities(cities)
                } else {
                    Log.e("CitySearch", "Failed to load cities")
                }
            }
        }
    }

    private fun filterCities(query: String, cities: List<City>) {
        val filteredCities = cities.filter { it.name.contains(query, ignoreCase = true) }
        cityAdapter.updateCities(filteredCities)
    }

    private fun showAddToFavoritesDialog(city: City) {
        AlertDialog.Builder(this, R.style.AppTheme_Dialog_Alert)
            .setTitle("Add to Favorites")
            .setMessage("Do you want to add ${city.name} to your favorites?")
            .setPositiveButton("Yes") { _, _ ->
                addToFavorites(city)
                updateFavoriteCitiesView()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun addToFavorites(city: City) {
        if (!FavoriteCitiesRepository.favoriteCities.contains(city)) {
            FavoriteCitiesRepository.favoriteCities.add(city)
            Log.d("Favorites", "City added to favorites: ${city.name}")
        } else {
            Log.d("Favorites", "City already in favorites: ${city.name}")
        }
    }

    private fun showFavoriteCityDialog(city: City) {
        AlertDialog.Builder(this, R.style.AppTheme_Dialog_Alert)
            .setTitle("Действия с городом")
            .setMessage("Что вы хотите сделать с ${city.name}?")
            .setPositiveButton("Переключиться на этот город") { _, _ ->
                switchToCity(city) // Теперь переключение происходит через общий метод
                Log.d("FavoriteCity", "Выбран город: ${city.name}")
            }
            .setNeutralButton("Отмена", null)
            .setNegativeButton("Убрать из избранного") { _, _ ->
                removeCityFromFavorites(city)
                updateFavoriteCitiesView()
            }
            .show()
    }


    private fun removeCityFromFavorites(city: City) {
        FavoriteCitiesRepository.favoriteCities.remove(city)
        binding.favoriteCitiesRecyclerView.adapter?.notifyDataSetChanged()
        Log.d("Favorites", "City removed from favorites: ${city.name}")
    }

    private fun switchToCity(city: City) {
        FavoriteCitiesRepository.selectCity(city)
        finish() // Закрываем текущую Activity
    }

    private fun updateFavoriteCitiesView() {
        favoriteCityAdapter.notifyDataSetChanged()
    }
}
