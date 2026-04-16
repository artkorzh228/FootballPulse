package com.artsiom.footballpulse.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.artsiom.footballpulse.BuildConfig

object RetrofitInstance {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-Auth-Token", BuildConfig.FOOTBALL_API_KEY)
                .build()
            chain.proceed(request)
        }
        .addInterceptor(logging)
        .build()

    val api: FootballApiService = Retrofit.Builder()
        .baseUrl("https://api.football-data.org/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FootballApiService::class.java)
}