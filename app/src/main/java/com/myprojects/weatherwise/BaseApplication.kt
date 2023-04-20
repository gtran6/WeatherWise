package com.myprojects.weatherwise

import android.app.Application
import com.myprojects.di.weatherModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            modules(weatherModules)
            androidContext(this@BaseApplication)
        }
    }
}