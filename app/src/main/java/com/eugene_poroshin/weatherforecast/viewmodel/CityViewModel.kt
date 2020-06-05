package com.eugene_poroshin.weatherforecast.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.eugene_poroshin.weatherforecast.repo.Repository
import com.eugene_poroshin.weatherforecast.repo.database.CityDatabase
import com.eugene_poroshin.weatherforecast.repo.database.CityEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository
    val allCitiesLiveData: LiveData<List<CityEntity>>

    init {
        val cityDao = CityDatabase.getDatabase(application, viewModelScope).cityDao()
        repository = Repository(cityDao)
        allCitiesLiveData = repository.allCities
    }

    fun insert(city: CityEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(city)
    }

    fun update(city: CityEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(city)
    }

    fun delete(city: CityEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(city)
    }

}