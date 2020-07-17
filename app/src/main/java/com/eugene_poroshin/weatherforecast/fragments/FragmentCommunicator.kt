package com.eugene_poroshin.weatherforecast.fragments

import com.eugene_poroshin.weatherforecast.repo.database.CityEntity

interface FragmentCommunicator {
    fun onItemClickListener(cityName: String?)
    fun onItemClickToDelete(cityEntity: CityEntity)
}