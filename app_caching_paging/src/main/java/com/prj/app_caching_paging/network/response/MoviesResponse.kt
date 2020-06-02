package com.prj.app_caching_paging.network.response

import com.google.gson.annotations.SerializedName
import com.prj.app_caching_paging.network.model.MovieData

data class MoviesResponse(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: List<MovieData>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)
