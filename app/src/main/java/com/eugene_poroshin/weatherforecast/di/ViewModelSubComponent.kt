package com.eugene_poroshin.weatherforecast.di

import com.eugene_poroshin.weatherforecast.viewmodel.CityViewModel
import dagger.Subcomponent

@Subcomponent
interface ViewModelSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): ViewModelSubComponent
    }

    fun inject(cityViewModel: CityViewModel)
}