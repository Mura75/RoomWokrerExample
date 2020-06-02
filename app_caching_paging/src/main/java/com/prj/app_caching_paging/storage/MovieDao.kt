package com.prj.app_caching_paging.storage

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prj.app_caching_paging.repository.Movie
import com.prj.app_caching_paging.storage.model.MovieEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
abstract class MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllTodos(list: List<MovieEntity>)

    @Query("SELECT * FROM movies")
    abstract fun getAllMovies(): Single<List<MovieEntity>>

    @Query("DELETE FROM movies")
    abstract fun deleteAll()

    @Query(" SELECT * FROM movies")
    abstract fun getAllMoviesPaged(): DataSource.Factory<Int, MovieEntity>
}