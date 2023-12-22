package com.example.newsapp.data.local

import com.example.newsapp.data.remote.FoodApi
import com.example.newsapp.data.remote.UserApi
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retro {
    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private fun getRetroClientInstance(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl("http://104.198.175.29:5000/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun createApiService(): UserApi {
        return getRetroClientInstance().create(UserApi::class.java)
    }
    fun createFoodService(): FoodApi {
        return getRetroClientInstance().create(FoodApi::class.java)
    }

}