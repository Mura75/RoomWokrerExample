package com.prj.app_caching_paging.di

import android.content.Context
import com.prj.app_caching_paging.storage.AppDatabase
import com.prj.app_caching_paging.storage.MovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object StorageModule {

    @Singleton
    @Provides
    internal fun provideDatabase(context: Context): AppDatabase = AppDatabase.getDatabase(context)

    @Singleton
    @Provides
    internal fun provideTodoDao(appDatabase: AppDatabase): MovieDao = appDatabase.getMovieDao()


}