package com.eugene_poroshin.weatherforecast.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.eugene_poroshin.weatherforecast.R;


public class TempModePreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.fragment_preference);
    }

}
