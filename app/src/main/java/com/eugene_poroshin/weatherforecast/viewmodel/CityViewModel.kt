package com.eugene_poroshin.weatherforecast.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.eugene_poroshin.weatherforecast.repo.Repository
import com.eugene_poroshin.weatherforecast.repo.database.CityEntity

class CityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository = Repository(application)
    val liveData: LiveData<MutableList<CityEntity>>

    fun insert(city: CityEntity?) {
        repository.insert(city)
    }

    fun update(city: CityEntity?) {
        repository.update(city)
    }

    fun delete(city: CityEntity?) {
        repository.delete(city)
    }

    init {
        liveData = repository.allCities
    }
}