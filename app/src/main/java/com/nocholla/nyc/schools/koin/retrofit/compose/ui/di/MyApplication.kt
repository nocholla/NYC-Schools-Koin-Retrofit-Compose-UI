package com.nocholla.nyc.schools.koin.retrofit.compose.ui.di

import android.app.Application
import com.nocholla.nyc.schools.koin.retrofit.compose.ui.data.api.OkHttpClientFactory
import com.nocholla.nyc.schools.koin.retrofit.compose.ui.data.api.SchoolsApiService
import com.nocholla.nyc.schools.koin.retrofit.compose.ui.data.local.database.SchoolDatabase
import com.nocholla.nyc.schools.koin.retrofit.compose.ui.data.repository.SchoolRepositoryImpl
import com.nocholla.nyc.schools.koin.retrofit.compose.ui.domain.usecase.GetSchoolsUseCase
import com.nocholla.nyc.schools.koin.retrofit.compose.ui.domain.usecase.GetScoresUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }
}

val appModule = module {
    single { OkHttpClientFactory().createOkHttpClient() }
    single { com.google.gson.Gson() }
    single {
        retrofit2.Retrofit.Builder()
            .baseUrl("https://data.cityofnewyork.us/resource/")
            .client(get())
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create(get()))
            .build()
    }
    single { get<retrofit2.Retrofit>().create(SchoolsApiService::class.java) }
    single {
        androidx.room.Room.databaseBuilder(
            androidContext(),
            SchoolDatabase::class.java,
            "school_database"
        ).build()
    }
    single { get<SchoolDatabase>().schoolDao() }
    single { get<SchoolDatabase>().scoreDao() }
    single { SchoolRepositoryImpl() }
    factory { GetSchoolsUseCase() }
    factory { GetScoresUseCase() }
}