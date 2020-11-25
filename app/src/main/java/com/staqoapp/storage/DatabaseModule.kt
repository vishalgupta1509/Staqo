package com.staqoapp.storage

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    fun provideDatabase(application: Application): StaqoDatabase {
        return Room.databaseBuilder(application, StaqoDatabase::class.java, "countries")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideCountriesDao(database: StaqoDatabase): MainDao {
        return  database.mainDao
    }

    single { provideDatabase(androidApplication()) }
    single { provideCountriesDao(get()) }
}