package com.bannanguy.task1androidapp.data.api.weather

import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class RetrofitClient(okHttpClient: OkHttpClient, baseUrl: String) {
    private var okHttpClient: OkHttpClient
    private var baseUrl: String

    init {
        this.okHttpClient = okHttpClient
        this.baseUrl = baseUrl
    }

    fun getClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object RetrofitClientFactory {
    private const val WEATHERAPI_BASE_URL = "https://api.weatherapi.com/v1/"
    private const val WEATHERAPI_CACHE_SIZE = 15L * 60L // 15 minutes in seconds

    private var retrofitClientSingleton: RetrofitClient? = null

    fun createBySingleton(type: String, cacheDir: File): RetrofitClient {
        return when (type) {
            "weatherapi" -> {
                if (retrofitClientSingleton == null) {
                    retrofitClientSingleton = create(type, cacheDir)
                }
                retrofitClientSingleton!!
            }
            else -> {
                throw Exception("Unknown type of Retrofit client.")
            }
        }
    }
    private fun create(type: String, cacheDir: File): RetrofitClient {
        when (type) {
            "weatherapi" -> {
                val cache = createCache(
                    cacheDir,
                    WEATHERAPI_CACHE_SIZE
                )

                return RetrofitClient(
                    createOkHttpClient(
                        cache
                    ),
                    WEATHERAPI_BASE_URL
                )
            }
            else -> {
                throw Exception("Unknown type of Retrofit client.")
            }
        }
    }

    private fun createCache(cacheDir: File, size: Long): Cache {
        return Cache(cacheDir, size)
    }

    private fun createOkHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .build()
    }
}