package com.prj.app_caching_paging.di

import com.prj.app_caching_paging.network.MovieApi
import com.prj.app_caching_paging.repository.MovieRepository
import com.prj.app_caching_paging.repository.MovieRepositoryImpl
import com.prj.app_caching_paging.storage.MovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        moveApi: MovieApi,
        moveDao: MovieDao
    ): MovieRepository = MovieRepositoryImpl(moveApi, moveDao)
}