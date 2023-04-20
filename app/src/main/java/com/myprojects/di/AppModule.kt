package com.myprojects.di

import androidx.room.Room
import com.myprojects.data.database.AppDatabase
import com.myprojects.data.local.LocalSource
import com.myprojects.data.local.LocalSourceImpl
import com.myprojects.data.preferences.PreferenceInterface
import com.myprojects.data.preferences.PreferenceProvider
import com.myprojects.data.remote.ConnectionProvider
import com.myprojects.data.remote.RemoteSource
import com.myprojects.repository.Repository
import com.myprojects.repository.RepositoryInterface
import com.myprojects.ui.home.HomeViewModel
import com.myprojects.ui.map.MapViewModel
import com.myprojects.ui.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val weatherModules = module {
    single<RepositoryInterface> {
        Repository(
            remoteSource = get(),
            localSource = get(),
            preferences = get()
        )
    }

    single<PreferenceInterface> {
        PreferenceProvider(androidContext())
    }

    single<RemoteSource>{
        ConnectionProvider
    }

    single<LocalSource> {
        LocalSourceImpl(get())
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "WEATHER_APP_DB"
        ).allowMainThreadQueries().build()
    }

    viewModel {
        SplashViewModel(get())
    }

    viewModel {
        HomeViewModel(get())
    }

    viewModel {
        MapViewModel(get())
    }
}