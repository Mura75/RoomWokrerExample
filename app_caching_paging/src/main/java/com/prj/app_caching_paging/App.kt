package com.prj.app_caching_paging

import android.app.Application
import com.facebook.stetho.Stetho
import com.prj.app_caching_paging.di.AppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        appComponent = AppComponent.create(this)
    }
}