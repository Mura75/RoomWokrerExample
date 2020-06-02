package com.prj.app_caching_paging.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import com.prj.app_caching_paging.repository.Movie

interface Listing<T> {
    fun getBoundaryCallback(): LiveData<GenericBoundaryCallback>
    fun getDataSource(): LiveData<PagedList<Movie>>
    fun getNetworkState(): LiveData<NetworkState> = Transformations.switchMap(getBoundaryCallback()) { it.networkState }
}