package com.prj.app_caching_paging.repository

import androidx.paging.DataSource
import com.prj.app_caching_paging.network.model.MovieData
import com.prj.app_caching_paging.storage.model.MovieEntity
import io.reactivex.Completable
import io.reactivex.Single

interface MovieRepository {
    fun getMovies(page: Int): Single<Pair<Int, List<Movie>>>
    fun insetAll(list: List<Movie>)
    fun getAllLocal(): Single<List<Movie>>
    fun getAllLocalPaged(): DataSource.Factory<Int, Movie>
}