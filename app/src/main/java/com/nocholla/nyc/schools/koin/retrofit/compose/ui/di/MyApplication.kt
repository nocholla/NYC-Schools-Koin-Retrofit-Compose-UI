package com.nocholla.nyc.schools.koin.retrofit.compose.ui.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        /*
        startKoin {
            androidContext(this@MyApplication)
            modules(appModule)
        }
        */
    }
}