package com.eugene_poroshin.weatherforecast.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eugene_poroshin.weatherforecast.viewmodel.CityViewModel
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {

    @Provides
    fun cityViewModelProvider(fragment: Fragment): CityViewModel {
        return ViewModelProvider(fragment).get(CityViewModel::class.java)
    }
}