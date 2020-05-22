package com.eugene_poroshin.weatherforecast.weather

import androidx.preference.PreferenceManager
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class WeatherParser(private val data: String) {
    @get:Throws(JSONException::class)
    val parsedCurrentWeather: WeatherCurrent
        get() {
            val jsonObject = JSONObject(data)
            val jsonMain = jsonObject.getJSONObject("main")
            val temperature = jsonMain.getDouble("temp")
            val jsonWeather = jsonObject.getJSONArray("weather").getJSONObject(0)
            val description = jsonWeather.getString("description")
            val iconID = jsonWeather.getString("icon")
            return WeatherCurrent(description, temperature, iconID)
        }

    @get:Throws(JSONException::class)
    val parsedListForecastWeather: MutableList<WeatherForecast>
        get() {
            val forecastList: MutableList<WeatherForecast> =
                ArrayList()
            val jsonObject = JSONObject(data)
            val jsonList = jsonObject.getJSONArray("list")
            for (i in 1 until jsonList.length()) {
                val jsonCurrentForecast = jsonList.getJSONObject(i)
                val timeMillis = jsonCurrentForecast.getLong("dt")
                val date = Date(timeMillis * 1000)
                val jsonWeather =
                    jsonCurrentForecast.getJSONArray("weather").getJSONObject(0)
                val description = jsonWeather.getString("description")
                val iconID = jsonWeather.getString("icon")
                val jsonTemp = jsonCurrentForecast.getJSONObject("temp")
                val temperature = jsonTemp.getDouble("day")
                val currentForecast =
                    WeatherForecast(date, iconID, description, temperature)
                forecastList.add(currentForecast)
            }
            return forecastList
        }

}