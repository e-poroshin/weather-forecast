package com.eugene_poroshin.weatherforecast.di

import com.eugene_poroshin.weatherforecast.fragments.CityListFragment
import dagger.Component
import javax.inject.Singleton


@Component(modules = [ApplicationModule::class, ViewModelModule::class])
@Singleton
interface AppComponent {

    fun inject(fragment: CityListFragment)
}