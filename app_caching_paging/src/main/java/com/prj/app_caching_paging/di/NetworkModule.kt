package com.prj.app_caching_paging.di

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.prj.app_caching_paging.network.AuthInterceptor
import com.prj.app_caching_paging.network.MovieApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object NetworkModule {

    private const val CONNECTION_INTERCEPTOR = "connection_interceptor"
    private const val AUTH_INTERCEPTOR = "auth_interceptor"
    private const val LOGGING_INTERCEPTOR = "logging_interceptor"

    private const val TIMEOUT = 60L

    private const val MOVIE_API_URL = "https://api.themoviedb.org/3/"

    @Provides
    @Reusable
    fun provideApiService(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MOVIE_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGson() = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttp(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
//        if (BuildConfig.DEBUG) {
//            okHttpClient.addInterceptor(httpLoggingInterceptor)
//        }
        return okHttpClient.build()
    }


    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(logger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("OkHttp", message)
            }
        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor()
    }
}