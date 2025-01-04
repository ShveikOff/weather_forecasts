package com.example.weatherforecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


object FavoriteCitiesRepository {
    val favoriteCities = mutableListOf<City>()

    private val _selectedCity = MutableLiveData<City>()
    val selectedCity: LiveData<City> get() = _selectedCity

    fun selectCity(city: City) {
        _selectedCity.value = city
    }
}
