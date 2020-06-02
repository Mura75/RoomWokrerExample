package com.prj.app_caching_paging.repository

import android.util.Log
import androidx.paging.DataSource
import com.prj.app_caching_paging.network.MovieApi
import com.prj.app_caching_paging.network.model.MovieData
import com.prj.app_caching_paging.storage.MovieDao
import com.prj.app_caching_paging.storage.model.MovieEntity
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) : MovieRepository {

    override fun getMovies(page: Int): Single<Pair<Int, List<Movie>>> {
        return movieApi.getPopularMovies(page)
            .flatMap { response ->
                if (response.isSuccessful) {
                    val pages = response.body()?.page ?: 1
                    val list = response.body()?.results ?: emptyList()
                    val pair = Pair(pages, list)
                    Single.just(pair)
                } else {
                    Single.error(Throwable("movie list error"))
                }
            }
            .map { pair ->
                val list = pair.second.map { item ->
                    Movie(
                        id = item.id,
                        adult = item.adult,
                        backdropPath = item.backdropPath,
                        originalLanguage = item.originalLanguage,
                        originalTitle = item.originalTitle,
                        overview = item.overview,
                        popularity = item.popularity,
                        posterPath = item.posterPath,
                        releaseDate = item.releaseDate,
                        title = item.title,
                        video = item.video,
                        voteAverage = item.voteAverage,
                        voteCount = item.voteCount
                    )
                }
                Pair(pair.first, list)
            }
    }

    override fun insetAll(list: List<Movie>) {
        movieDao.insertAllTodos(list.map { item ->
            MovieEntity(
                id = item.id,
                adult = item.adult,
                backdropPath = item.backdropPath,
                originalLanguage = item.originalLanguage,
                originalTitle = item.originalTitle,
                overview = item.overview,
                popularity = item.popularity,
                posterPath = item.posterPath,
                releaseDate = item.releaseDate,
                title = item.title,
                video = item.video,
                voteAverage = item.voteAverage,
                voteCount = item.voteCount
            )
        })
    }

    override fun getAllLocal(): Single<List<Movie>> {
        return Single.just(emptyList())
    }

    override fun getAllLocalPaged(): DataSource.Factory<Int, Movie> {
        return movieDao.getAllMoviesPaged()
            .map { item ->
                Movie(
                    id = item.id,
                    adult = item.adult,
                    backdropPath = item.backdropPath,
                    originalLanguage = item.originalLanguage,
                    originalTitle = item.originalTitle,
                    overview = item.overview,
                    popularity = item.popularity,
                    posterPath = item.posterPath,
                    releaseDate = item.releaseDate,
                    title = item.title,
                    video = item.video,
                    voteAverage = item.voteAverage,
                    voteCount = item.voteCount
                )
            }
    }
}