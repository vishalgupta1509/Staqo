package com.staqoapp.di

import com.staqoapp.appScreens.MainViewModel
import com.staqoapp.repositories.MainRepository
import com.staqoapp.utils.AppExecutors
import com.staqoapp.utils.ConnectionDetector
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val module = module {
        single { AppExecutors() }
        single { ConnectionDetector(get()) }
    }

    val viewModels = module {
        factory { MainRepository(get()) }
        viewModel { MainViewModel(get()) }
    }
}