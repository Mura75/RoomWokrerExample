package com.prj.testroom.di

import android.app.Application
import com.prj.testroom.TodoApp
import com.prj.testroom.worker.TodoWorker
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules =[
        AndroidInjectionModule::class,
        StorageModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent : AndroidInjector<TodoApp> {

    fun inject(worker: TodoWorker)

    companion object {
        fun create(application: Application): AppComponent {
            return DaggerAppComponent.builder()
                .application(application)
                .storageModule(StorageModule(application))
                .repositoryModule(RepositoryModule())
                .build()
        }
    }

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun storageModule(storageModule: StorageModule):Builder

        fun repositoryModule(repositoryModule: RepositoryModule): Builder

        fun build(): AppComponent
    }

}
