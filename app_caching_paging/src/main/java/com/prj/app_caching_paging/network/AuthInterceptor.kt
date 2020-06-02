package com.prj.app_caching_paging.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(): Interceptor {

    companion object {
        private const val API_KEY = "72679493d1d20e8ef28849f2dd761c5a"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl = chain.request().url
            .newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .build()
        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }
}