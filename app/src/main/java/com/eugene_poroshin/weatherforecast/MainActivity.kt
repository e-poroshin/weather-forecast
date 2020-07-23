package com.eugene_poroshin.weatherforecast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.eugene_poroshin.weatherforecast.fragments.OnOpenFragmentListener

class MainActivity : AppCompatActivity(), OnOpenFragmentListener {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return
        navController = host.navController
    }

    override fun onOpenCityListFragment() {
        navController.navigate(R.id.cityListFragment)
    }

    override fun onOpenTempModePreferenceFragment() {
        navController.navigate(R.id.tempModePreferenceFragment)
    }
}