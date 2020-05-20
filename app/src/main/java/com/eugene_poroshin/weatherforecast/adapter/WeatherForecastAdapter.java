package com.eugene_poroshin.weatherforecast.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eugene_poroshin.weatherforecast.R;
import com.eugene_poroshin.weatherforecast.weather.Constants;
import com.eugene_poroshin.weatherforecast.weather.WeatherForecast;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.RecyclerViewHolder> {

    private List<WeatherForecast> forecastList;

    public WeatherForecastAdapter(List<WeatherForecast> forecast) {
        this.forecastList = new ArrayList<>(forecast);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Date date = forecastList.get(position).getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E,-MMM dd");
        String dateText = dateFormat.format(date);
        String[] parts = dateText.split("-");
        String dayOfWeek = parts[0];
        String dayAndMonth = parts[1];
        holder.textViewForecastDayOfWeek.setText(dayOfWeek);
        holder.textViewForecastDayAndMonth.setText(dayAndMonth);
        String imageUrl = String.format(Constants.GET_ICON, forecastList.get(position).getIconId());
        Picasso.get().load(imageUrl).into(holder.forecastWeatherIcon);
        holder.textViewForecastTemperature.setText(new DecimalFormat("##.#").format(forecastList.get(position).getTemperature()));
        holder.textViewForecastDescription.setText(forecastList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        if (forecastList != null) {
            return forecastList.size();
        } else
            return 0;
    }

    public void setForecast(List<WeatherForecast> forecast) {
        forecastList = forecast;
        notifyDataSetChanged();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewForecastDayOfWeek;
        private TextView textViewForecastDayAndMonth;
        private ImageView forecastWeatherIcon;
        private TextView textViewForecastTemperature;
        private TextView textViewForecastDescription;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewForecastDayOfWeek = itemView.findViewById(R.id.itemTextForecastNumberOfDay);
            textViewForecastDayAndMonth = itemView.findViewById(R.id.itemTextForecastDay);
            forecastWeatherIcon = itemView.findViewById(R.id.itemForecastWeatherIcon);
            textViewForecastTemperature = itemView.findViewById(R.id.itemTextForecastTemperature);
            textViewForecastDescription = itemView.findViewById(R.id.itemTextForecastDescription);
        }
    }
}
