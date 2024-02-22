package com.bannanguy.task1androidapp.api.weather

import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object RetrofitClient {
    private const val BASE_URL = "https://api.weatherapi.com/v1/"
    private const val CACHE_SIZE = 15 * 60 // 15 minutes in seconds

    private lateinit var cache: Cache

    fun setupCache(cacheDir: File) {
        if (::cache.isInitialized) return
        cache = Cache(cacheDir, CACHE_SIZE.toLong())
    }

    fun getClient(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
