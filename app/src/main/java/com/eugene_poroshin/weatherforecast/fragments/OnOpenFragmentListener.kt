package com.eugene_poroshin.weatherforecast.fragments

interface OnOpenFragmentListener {
    fun onOpenForecastFragment()
    fun onOpenForecastFragmentByCityName(newCityName: String?)
    fun onOpenCityListFragment()
    fun onOpenTempModePreferenceFragment()
}