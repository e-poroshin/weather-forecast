package com.eugene_poroshin.weatherforecast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eugene_poroshin.weatherforecast.fragments.CityListFragment
import com.eugene_poroshin.weatherforecast.fragments.ForecastFragment
import com.eugene_poroshin.weatherforecast.fragments.OnOpenFragmentListener
import com.eugene_poroshin.weatherforecast.fragments.TempModePreferenceFragment

class MainActivity : AppCompatActivity(), OnOpenFragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onOpenForecastFragment()
    }

    override fun onOpenForecastFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentContainer,
                ForecastFragment(),
                ForecastFragment::class.java.simpleName
            )
            .commit()
    }

    override fun onOpenForecastFragmentByCityName(newCityName: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentContainer,
                ForecastFragment.newInstance(newCityName),
                ForecastFragment::class.java.simpleName
            )
            .commit()
    }

    override fun onOpenCityListFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentContainer,
                CityListFragment.newInstance(),
                CityListFragment::class.java.simpleName
            )
            .commit()
    }

    override fun onOpenTempModePreferenceFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentContainer,
                TempModePreferenceFragment(),
                TempModePreferenceFragment::class.java.simpleName
            )
            .addToBackStack(null)
            .commit()
    }
}