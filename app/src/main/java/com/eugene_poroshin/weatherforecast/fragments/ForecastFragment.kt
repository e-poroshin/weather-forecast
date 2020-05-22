package com.eugene_poroshin.weatherforecast.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eugene_poroshin.weatherforecast.R
import com.eugene_poroshin.weatherforecast.adapter.WeatherForecastAdapter
import com.eugene_poroshin.weatherforecast.weather.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import java.text.DecimalFormat
import java.util.*

class ForecastFragment : Fragment() {

    private val SAVED_CITY = "SAVED_CITY"
    private var viewWeatherIcon: ImageView? = null
    private var textViewTemperature: TextView? = null
    private var textViewTempMode: TextView? = null
    private var textViewDescriptionWeather: TextView? = null
    private var textViewCityName: TextView? = null
    private var toolbar: Toolbar? = null
    private var cityName: String? = null
    private val listWeatherForecast: MutableList<WeatherForecast> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var adapter: WeatherForecastAdapter? = null
    private var sPref: SharedPreferences? = null
    private var onOpenFragmentListener: OnOpenFragmentListener? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnOpenFragmentListener) {
            onOpenFragmentListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedCityName != "NO_SAVED") {
            cityName = savedCityName
        }
        if (arguments != null) {
            cityName = requireArguments().getString("CITY_NAME")
            saveCityName(cityName)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forecast, container, false)
        viewWeatherIcon = view.findViewById(R.id.viewWeatherIcon)
        textViewTemperature = view.findViewById(R.id.textViewTemperature)
        textViewTempMode = view.findViewById(R.id.textViewTempMode)
        textViewDescriptionWeather = view.findViewById(R.id.textViewDescriptionWeather)
        textViewCityName = view.findViewById(R.id.textViewCityName)
        view.findViewById<View>(R.id.buttonTemperatureMode)
            .setOnClickListener {
                if (onOpenFragmentListener != null) {
                    onOpenFragmentListener!!.onOpenTempModePreferenceFragment()
                }
            }
        recyclerView = view.findViewById(R.id.recycler_view_forecast_list)
        adapter = WeatherForecastAdapter(listWeatherForecast)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = adapter
        toolbar = view.findViewById(R.id.my_toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        return view
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val tempMode = sharedPreferences.getBoolean("switch", false)
        if (cityName != null) {
            textViewCityName!!.text = cityName
            getCurrentWeather(cityName!!, tempMode)
            getForecastWeather(cityName!!, tempMode)
        }
    }

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.forecast_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add) {
            if (onOpenFragmentListener != null) {
                onOpenFragmentListener!!.onOpenCityListFragment()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getCurrentWeather(cityName: String, tempMode: Boolean) {
        val apiKey = Constants.API_KEY
        val url: String = if (!tempMode) {
            String.format(Constants.GET_CURRENT_WEATHER_BY_CITY_NAME_METRIC, cityName, apiKey)
        } else {
            String.format(Constants.GET_CURRENT_WEATHER_BY_CITY_NAME_IMPERIAL, cityName, apiKey)
        }

        lifecycleScope.launch {
            val result = getResponse(url)
            val weatherParser = WeatherParser(result)
            try {
                val weatherCurrent = weatherParser.parsedCurrentWeather
                showCurrentWeather(weatherCurrent, tempMode)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun showCurrentWeather(weatherCurrent: WeatherCurrent, tempMode: Boolean) {
        val imageUrl = String.format(
            Constants.GET_ICON,
            weatherCurrent.iconId
        )
        //TODO change to Koin
        Picasso.get().load(imageUrl).into(viewWeatherIcon)
        textViewDescriptionWeather!!.text = weatherCurrent.description
        textViewTemperature!!.text = DecimalFormat("##.#").format(weatherCurrent.temperature)
        if (!tempMode) {
            textViewTempMode!!.text = "℃"
        } else {
            textViewTempMode!!.text = "℉"
        }
    }

    private fun getForecastWeather(cityName: String, tempMode: Boolean) {
        val temperatureMode: TemperatureMode
        val apiKey = Constants.API_KEY_FORECAST
        var url: String = ""
        if (!tempMode) {
            url = String.format(Constants.GET_FORECAST_WEATHER_BY_CITY_NAME_METRIC, cityName, apiKey)
            temperatureMode = TemperatureMode.CELSIUS
        } else {
            url = String.format(Constants.GET_FORECAST_WEATHER_BY_CITY_NAME_IMPERIAL, cityName, apiKey)
            temperatureMode = TemperatureMode.FAHRENHEIT
        }

        lifecycleScope.launch {
            val result = getResponse(url)
            val weatherParser = WeatherParser(result)
            try {
                val listWeatherForecast = weatherParser.parsedListForecastWeather
                listWeatherForecast[0].temperatureMode = temperatureMode
                showForecastWeather(listWeatherForecast)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun showForecastWeather(listWeatherForecasts: MutableList<WeatherForecast>) {
        adapter!!.setForecast(listWeatherForecasts)
    }

    private suspend fun getResponse(myURL: String): String {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder().url(myURL).build()
            val okHttpClient = OkHttpClient()
            val str = okHttpClient.newCall(request).execute().body!!.string()
            str
        }
    }

    private fun saveCityName(cityName: String?) {
        sPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val editor = sPref?.edit()
        editor!!.putString(SAVED_CITY, cityName)
        editor.apply()
    }

    private val savedCityName: String?
        get() {
            sPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            return sPref?.getString(SAVED_CITY, "NO_SAVED")
        }

    override fun onDestroy() {
        super.onDestroy()
        onOpenFragmentListener = null
    }

    companion object {
        fun newInstance(cityName: String?): ForecastFragment {
            val forecastFragment = ForecastFragment()
            val bundle = Bundle()
            bundle.putString("CITY_NAME", cityName)
            forecastFragment.arguments = bundle
            return forecastFragment
        }
    }
}