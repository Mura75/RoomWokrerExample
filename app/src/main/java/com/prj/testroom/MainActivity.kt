package com.prj.testroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.*
import com.prj.testroom.worker.TodoWorker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text.setOnClickListener {
            Log.d("save_data", "start worker")
//            oneTimeWorkRequestBuilder(
//                worker = TodoWorker::class.java
//            )
            addToWorkToQueue(
                worker = TodoWorker::class.java,
                tag = TodoWorker.TAG
            )
        }
    }

    fun addToWorkToQueue(worker: Class<out ListenableWorker>, tag :String, periodMinutes: Long = 15) {
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.KEEP,
            createPeriodicWorkRequest(worker, periodMinutes)
        )
    }

    private fun createPeriodicWorkRequest(
        worker: Class<out ListenableWorker>,
        periodMinutes: Long
    ): PeriodicWorkRequest {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        return PeriodicWorkRequest.Builder(worker, periodMinutes, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
    }

    private fun oneTimeWorkRequestBuilder(worker: Class<out ListenableWorker>): OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        return OneTimeWorkRequest.Builder(worker)
            .setConstraints(constraints)
            .build()
    }

}
