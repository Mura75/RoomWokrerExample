package com.prj.app_caching_paging.di

import android.app.Application
import android.content.Context
import com.prj.app_caching_paging.MainActivity
import com.prj.app_caching_paging.storage.AppDatabase
import com.prj.app_caching_paging.storage.MovieDao
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules =[
        NetworkModule::class,
        RepositoryModule::class,
        StorageModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {

    fun inject(activity: MainActivity)

    fun getAppDatabase(): AppDatabase
    fun getMovieDao(): MovieDao

    companion object {
        fun create(application: Application): AppComponent {
            return DaggerAppComponent.builder()
                .applicationContext(application.applicationContext)
                .build()
        }
    }

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun applicationContext(context: Context): Builder

        fun build(): AppComponent
    }
}