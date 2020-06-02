package com.prj.app_caching_paging.network

import com.prj.app_caching_paging.network.response.MoviesResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int) : Single<Response<MoviesResponse>>

}