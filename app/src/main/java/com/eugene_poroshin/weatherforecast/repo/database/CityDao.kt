package com.eugene_poroshin.weatherforecast.repo.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: CityEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(city: CityEntity)

    @Delete
    suspend fun delete(city: CityEntity)

    @Query("SELECT * FROM cities ORDER BY name ASC")
    fun getAllCities(): LiveData<List<CityEntity>>
}