package com.eugene_poroshin.weatherforecast.repo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
class CityEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String?
)