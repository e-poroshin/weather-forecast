package com.eugene_poroshin.weatherforecast.repo

import androidx.lifecycle.LiveData
import com.eugene_poroshin.weatherforecast.repo.database.CityDao
import com.eugene_poroshin.weatherforecast.repo.database.CityEntity

class Repository(private val cityDao: CityDao) {

    val allCities: LiveData<List<CityEntity>> = cityDao.getAllCities()

    suspend fun insert(city: CityEntity) {
        cityDao.insert(city)
    }

    suspend fun delete(city: CityEntity) {
        cityDao.delete(city)
    }

    suspend fun update(city: CityEntity) {
        cityDao.update(city)
    }

}