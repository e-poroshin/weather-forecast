package com.eugene_poroshin.weatherforecast.weather

import java.util.*

data class WeatherForecast(
    val date: Date,
    val iconId: String,
    val description: String,
    val temperature: Double
)