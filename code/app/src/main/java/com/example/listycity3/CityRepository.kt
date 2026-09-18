package com.example.listycity3

import androidx.compose.runtime.mutableStateListOf

// Backs the list with a SnapshotStateList (mutableStateListOf) instead of a plain listOf, so any
// composable reading cities recomposes automatically on add or update - no manual refresh needed.
class CityRepository {
    private val _cities = mutableStateListOf(
        City("Edmonton", "AB"),
        City("Vancouver", "BC"),
        City("Toronto", "ON"),
    )

    val cities: List<City>
        get() = _cities

    fun addCity(city: City) {
        _cities.add(city)
    }

    // City only has val properties, so an existing entry can't be edited in place. Swapping in a
    // new City at the same index is what keeps the edit from changing the row's position.
    fun updateCity(oldCity: City, updatedCity: City) {
        val index = _cities.indexOf(oldCity)
        if (index != -1) {
            _cities[index] = updatedCity
        }
    }
}
