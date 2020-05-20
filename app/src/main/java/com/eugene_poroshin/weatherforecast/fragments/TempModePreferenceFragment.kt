package com.eugene_poroshin.weatherforecast.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.eugene_poroshin.weatherforecast.R

class TempModePreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        addPreferencesFromResource(R.xml.fragment_preference)
    }
}