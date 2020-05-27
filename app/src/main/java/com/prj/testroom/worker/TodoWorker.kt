package com.prj.testroom.worker

import android.content.Context
import android.util.Log
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.prj.testroom.TodoApp
import com.prj.testroom.TodoRepositoryImpl
import io.reactivex.Single
import javax.inject.Inject

class TodoWorker (
    context: Context,
    workerParameters: WorkerParameters
) : RxWorker(context, workerParameters) {

    companion object {
        const val TAG: String = "TODO_WORKER"
    }

    @Inject
    lateinit var todoRepositoryImpl: TodoRepositoryImpl

    init {
        (context.applicationContext as TodoApp).appComponent.inject(this)
        Log.d("save_data", "init")
    }

    override fun createWork(): Single<Result> {
        Log.d("save_data", "save some todo list")
        todoRepositoryImpl.saveAll(listOf())
        return Single.just(Result.success())
    }


}