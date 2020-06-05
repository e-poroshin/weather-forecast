package com.eugene_poroshin.weatherforecast.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.eugene_poroshin.weatherforecast.fragments.CityListFragment
import com.eugene_poroshin.weatherforecast.viewmodel.CityViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides
    fun cityViewModelProvider(application: Application): CityViewModel {
        return CityViewModel(application)
    }
}