package com.eugene_poroshin.weatherforecast.weather

object Constants {
    const val GET_CURRENT_WEATHER_BY_CITY_NAME_METRIC =
        "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric"
    const val GET_FORECAST_WEATHER_BY_CITY_NAME_METRIC =
        "https://api.openweathermap.org/data/2.5/forecast/daily?q=%s&units=metric&cnt=6&appid=%s"
    const val GET_CURRENT_WEATHER_BY_CITY_NAME_IMPERIAL =
        "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=imperial"
    const val GET_FORECAST_WEATHER_BY_CITY_NAME_IMPERIAL =
        "https://api.openweathermap.org/data/2.5/forecast/daily?q=%s&units=imperial&cnt=6&appid=%s"
    const val GET_ICON = "https://openweathermap.org/img/wn/%s@2x.png"
    const val API_KEY = "7b01a2dc6748b4211e6230b2b77d9dab"
    const val API_KEY_FORECAST = "9de243494c0b295cca9337e1e96b00e2"
}