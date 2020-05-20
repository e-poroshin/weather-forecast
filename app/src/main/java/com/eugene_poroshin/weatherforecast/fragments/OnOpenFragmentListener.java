package com.eugene_poroshin.weatherforecast.fragments;

public interface OnOpenFragmentListener {
    void onOpenForecastFragment();
    void onOpenForecastFragmentByCityName(String newCityName);
    void onOpenCityListFragment();
    void onOpenTempModePreferenceFragment();
}
