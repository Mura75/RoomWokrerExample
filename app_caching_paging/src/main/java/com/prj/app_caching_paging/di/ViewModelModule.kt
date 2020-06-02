package com.prj.app_caching_paging.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prj.app_caching_paging.MovieListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(
        factory: DaggerViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MovieListViewModel::class)
    internal abstract fun provideMovieListViewModel(viewModel: MovieListViewModel): ViewModel

}