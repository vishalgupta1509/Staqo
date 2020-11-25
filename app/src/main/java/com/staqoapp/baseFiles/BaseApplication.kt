package com.staqoapp.baseFiles

import android.app.Application
import com.staqoapp.di.AppModule
import com.staqoapp.network.networkModule
import com.staqoapp.storage.databaseModule
import org.koin.android.java.KoinAndroidApplication
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        val koin = KoinAndroidApplication.create(this)
            .logger(AndroidLogger(Level.ERROR))
            .modules(
                AppModule.module,
                AppModule.viewModels,
                networkModule,
                databaseModule
            )

        startKoin(
            GlobalContext(), koin
        )
    }
}