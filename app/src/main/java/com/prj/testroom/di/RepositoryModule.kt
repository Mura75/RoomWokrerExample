package com.prj.testroom.di

import com.prj.testroom.TodoRepositoryImpl
import com.prj.testroom.database.TodoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideTodoRepository(todoDao: TodoDao): TodoRepositoryImpl = TodoRepositoryImpl(todoDao)
}