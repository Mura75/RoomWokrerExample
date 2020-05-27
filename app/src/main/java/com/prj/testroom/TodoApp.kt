package com.prj.testroom

import android.app.Application
import com.prj.testroom.di.AppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class TodoApp : Application(), HasAndroidInjector {

    @Inject
    internal lateinit var dispatchingAndroidInjectorAny: DispatchingAndroidInjector<Any>

    val appComponent by lazy {
        AppComponent.create(this)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjectorAny
}