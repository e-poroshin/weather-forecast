package com.eugene_poroshin.weatherforecast.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eugene_poroshin.weatherforecast.R
import com.eugene_poroshin.weatherforecast.weather.Constants
import com.eugene_poroshin.weatherforecast.weather.WeatherForecast
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class WeatherForecastAdapter(forecast: MutableList<WeatherForecast>) :
    RecyclerView.Adapter<WeatherForecastAdapter.RecyclerViewHolder>() {

    private var forecastList: MutableList<WeatherForecast>?

    init {
        forecastList = ArrayList(forecast)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.forecast_item, parent, false)
        return RecyclerViewHolder(
            view
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(
        holder: RecyclerViewHolder,
        position: Int
    ) {
        val date = forecastList!![position].date
        val dateFormat = SimpleDateFormat("E,-MMM dd")
        val dateText = dateFormat.format(date)
        val parts = dateText.split("-").toTypedArray()
        val dayOfWeek = parts[0]
        val dayAndMonth = parts[1]
        holder.textViewForecastDayOfWeek.text = dayOfWeek
        holder.textViewForecastDayAndMonth.text = dayAndMonth
        val imageUrl = String.format(
            Constants.GET_ICON,
            forecastList!![position].iconId
        )
        Picasso.get().load(imageUrl).into(holder.forecastWeatherIcon)
        holder.textViewForecastTemperature.text =
            DecimalFormat("##.#").format(forecastList!![position].temperature)
        holder.textViewForecastDescription.text = forecastList!![position].description
    }

    override fun getItemCount(): Int {
        return if (forecastList != null) {
            forecastList!!.size
        } else 0
    }

    fun setForecast(forecast: MutableList<WeatherForecast>) {
        forecastList = forecast
        notifyDataSetChanged()
    }

    inner class RecyclerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val textViewForecastDayOfWeek: TextView
        val textViewForecastDayAndMonth: TextView
        val forecastWeatherIcon: ImageView
        val textViewForecastTemperature: TextView
        val textViewForecastDescription: TextView

        init {
            textViewForecastDayOfWeek =
                itemView.findViewById(R.id.itemTextForecastNumberOfDay)
            textViewForecastDayAndMonth = itemView.findViewById(R.id.itemTextForecastDay)
            forecastWeatherIcon =
                itemView.findViewById(R.id.itemForecastWeatherIcon)
            textViewForecastTemperature =
                itemView.findViewById(R.id.itemTextForecastTemperature)
            textViewForecastDescription =
                itemView.findViewById(R.id.itemTextForecastDescription)
        }
    }


}