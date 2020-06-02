package com.prj.app_caching_paging.datasource

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.prj.app_caching_paging.repository.Movie
import com.prj.app_caching_paging.repository.MovieRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class GenericBoundaryCallback(
    private val movieRepository: MovieRepository,
    private val compositeDisposable: CompositeDisposable,
    private val pagingRequestHelper: PagingRequestHelper
): PagedList.BoundaryCallback<Movie>() {

    val networkState: MutableLiveData<NetworkState> = pagingRequestHelper.createStatusLiveData()

    private var offsetCount = 0

    @MainThread
    override fun onZeroItemsLoaded() {
        pagingRequestHelper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { callback ->
            getTop(offsetCount, callback)
        }
    }

    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        pagingRequestHelper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { callback ->
            getTop(offsetCount, callback)
        }
    }

    fun clear() {
        compositeDisposable.clear()
    }

    fun retryPetitions() = pagingRequestHelper.retryAllFailed()

    private fun getTop(offset: Int, pagingRequest: PagingRequestHelper.Request.Callback) {
        compositeDisposable.add(
            movieRepository.getMovies(if (offset == 0) 1 else offset)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { pair ->
                        movieRepository.insetAll(pair.second)
                        pagingRequest.recordSuccess()
                        offsetCount += 1
                    },
                    { error ->
                        networkState.postValue(NetworkState.error(error.message))
                        pagingRequest.recordFailure(error)
                    }
                )
        )
    }

}