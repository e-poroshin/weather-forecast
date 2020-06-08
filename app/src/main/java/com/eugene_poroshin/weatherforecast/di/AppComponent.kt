package com.eugene_poroshin.weatherforecast.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Component(modules = [FragmentModule::class])
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun whithApplication(application: Application): Builder

        fun build(): AppComponent
    }

    fun viewModelSubComponentBuilder(): ViewModelSubComponent.Builder
    fun fragmentSubComponentBuilder(): FragmentSubComponent.Builder
}