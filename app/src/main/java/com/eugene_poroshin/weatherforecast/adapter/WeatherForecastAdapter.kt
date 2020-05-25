package com.eugene_poroshin.weatherforecast.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.eugene_poroshin.weatherforecast.R
import com.eugene_poroshin.weatherforecast.weather.Constants
import com.eugene_poroshin.weatherforecast.weather.TemperatureMode
import com.eugene_poroshin.weatherforecast.weather.WeatherForecast
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class WeatherForecastAdapter(forecast: List<WeatherForecast>) :
    RecyclerView.Adapter<WeatherForecastAdapter.ForecastViewHolder>() {

    private var forecastList: List<WeatherForecast>

    init {
        forecastList = ArrayList(forecast)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ForecastViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.forecast_item, parent, false)
        return ForecastViewHolder(
            view
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(
        holder: ForecastViewHolder,
        position: Int
    ) {
        val date = forecastList[position].date
        val dateFormat = SimpleDateFormat("E,-MMM dd")
        val dateText = dateFormat.format(date)
        val parts = dateText.split("-").toTypedArray()
        val dayOfWeek = parts[0]
        val dayAndMonth = parts[1]
        holder.textViewForecastDayOfWeek.text = dayOfWeek
        holder.textViewForecastDayAndMonth.text = dayAndMonth
        val imageUrl = String.format(
            Constants.GET_ICON,
            forecastList[position].iconId
        )
        holder.forecastWeatherIcon.load(imageUrl)
        holder.textViewForecastTemperature.text =
            DecimalFormat("##.#").format(forecastList[position].temperature)
        if (forecastList[0].temperatureMode == TemperatureMode.CELSIUS) {
            holder.itemTextViewTempMode.text = "℃"
        } else {
            holder.itemTextViewTempMode.text = "℉"
        }
        holder.textViewForecastDescription.text = forecastList[position].description
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    fun setForecast(forecast: MutableList<WeatherForecast>) {
        forecastList = forecast
        notifyDataSetChanged()
    }

    inner class ForecastViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val textViewForecastDayOfWeek: TextView =
            itemView.findViewById(R.id.itemTextForecastNumberOfDay)
        val textViewForecastDayAndMonth: TextView = itemView.findViewById(R.id.itemTextForecastDay)
        val forecastWeatherIcon: ImageView = itemView.findViewById(R.id.itemForecastWeatherIcon)
        val textViewForecastTemperature: TextView =
            itemView.findViewById(R.id.itemTextForecastTemperature)
        val itemTextViewTempMode: TextView = itemView.findViewById(R.id.itemTextViewTempMode)
        val textViewForecastDescription: TextView =
            itemView.findViewById(R.id.itemTextForecastDescription)
    }
}