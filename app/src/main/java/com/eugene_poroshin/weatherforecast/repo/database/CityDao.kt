package com.eugene_poroshin.weatherforecast.repo.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(city: CityEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(city: CityEntity)

    @Delete
    fun delete(city: CityEntity)

    @get:Query("SELECT * FROM cities")
    val allCities: LiveData<List<CityEntity?>?>?
}