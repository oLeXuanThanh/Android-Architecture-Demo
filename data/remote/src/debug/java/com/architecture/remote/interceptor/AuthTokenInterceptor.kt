package com.architecture.remote.interceptor

import com.architecture.preference.UserDataStorePreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor(
    private val dataStorePreferences: UserDataStorePreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("Accept-Encoding", "gzip, deflate")
            .header("Content-Type", "application/json;charset=utf-8")
            .header("Accept", "application/json")
            .header("User-Agent", "android")

        val accessToken = runBlocking {
             dataStorePreferences.accessToken.first()
        }
        if (accessToken.isNotBlank()) {
            requestBuilder.header("Authorization", "Bearer $accessToken")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}