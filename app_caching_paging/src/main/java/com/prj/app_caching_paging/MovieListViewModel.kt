package com.prj.app_caching_paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.prj.app_caching_paging.datasource.GenericBoundaryCallback
import com.prj.app_caching_paging.datasource.Listing
import com.prj.app_caching_paging.datasource.PagingRequestHelper
import com.prj.app_caching_paging.repository.Movie
import com.prj.app_caching_paging.repository.MovieRepository
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executors
import javax.inject.Inject

class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val listingLiveData: LiveData<Listing<Movie>> by lazy {

        val listing = object : Listing<Movie> {

            val boundaryCallback: GenericBoundaryCallback by lazy {
                GenericBoundaryCallback(
                    movieRepository = repository,
                    compositeDisposable = compositeDisposable,
                    pagingRequestHelper = PagingRequestHelper(Executors.newSingleThreadExecutor())
                )
            }

            override fun getBoundaryCallback(): LiveData<GenericBoundaryCallback> {
                val liveData = MutableLiveData<GenericBoundaryCallback>()
                liveData.value = boundaryCallback
                return liveData
            }

            override fun getDataSource(): LiveData<PagedList<Movie>> {
                return repository.getAllLocalPaged()
                    .toLiveData(
                        pageSize = 20,
                        boundaryCallback = boundaryCallback
                    )
            }
        }
        val liveData = MutableLiveData<Listing<Movie>>()
        liveData.value = listing
        liveData
    }

    private val boundaryCallback = Transformations.switchMap(listingLiveData) { it.getBoundaryCallback() }
    val dataSourceLiveData = Transformations.switchMap(listingLiveData) { it.getDataSource() }
    val networkStateLiveData = Transformations.switchMap(listingLiveData) { it.getNetworkState() }

    fun retry() {
        boundaryCallback.value?.retryPetitions()
    }

    override fun onCleared() {
        boundaryCallback.value?.clear()
        compositeDisposable.clear()
        super.onCleared()
    }
}