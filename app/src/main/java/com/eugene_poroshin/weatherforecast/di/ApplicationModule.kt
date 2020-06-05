package com.eugene_poroshin.weatherforecast.di

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return Application()
    }
}