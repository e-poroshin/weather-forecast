package com.eugene_poroshin.weatherforecast.weather

data class WeatherCurrent(
    val description: String,
    val temperature: Double,
    val iconId: String
)