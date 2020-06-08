package com.eugene_poroshin.weatherforecast.di

import android.app.Application

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
        lateinit var viewModelSubComponent: ViewModelSubComponent
    }

    override fun onCreate() {
        super.onCreate()
        initializeDagger()
    }

    private fun initializeDagger() {
        appComponent = DaggerAppComponent
            .builder()
            .whithApplication(this)
            .build()

        viewModelSubComponent = appComponent
            .viewModelSubComponentBuilder()
            .build()
    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }

    fun getViewModelSubComponent(): ViewModelSubComponent {
        return viewModelSubComponent
    }
}