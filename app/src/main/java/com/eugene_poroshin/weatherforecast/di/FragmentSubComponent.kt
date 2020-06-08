package com.eugene_poroshin.weatherforecast.di

import androidx.fragment.app.Fragment
import com.eugene_poroshin.weatherforecast.fragments.CityListFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [FragmentModule::class])
interface FragmentSubComponent {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun whith(fragment: Fragment): Builder
        fun build(): FragmentSubComponent
    }

    fun inject(cityListFragment: CityListFragment)
}