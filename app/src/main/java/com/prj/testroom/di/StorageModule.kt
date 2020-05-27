package com.prj.testroom.di

import android.content.Context
import com.prj.testroom.database.AppDatabase
import com.prj.testroom.database.TodoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule(private val context: Context) {

    @Singleton
    @Provides
    internal fun provideDatabase(): AppDatabase = AppDatabase.getDatabase(context)

    @Singleton
    @Provides
    internal fun provideTodoDao(appDatabase: AppDatabase): TodoDao = appDatabase.getTodoDao()

}