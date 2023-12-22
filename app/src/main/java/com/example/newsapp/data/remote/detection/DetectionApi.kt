package com.example.newsapp.data.remote.detection

import com.example.newsapp.data.local.RetrofitDetectionConfig
import com.example.newsapp.data.model.detection.DetectionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


val detectionApiService = RetrofitDetectionConfig().createApiService()

interface DetectionApi {
    @Multipart
    @POST("predict/")
    fun detectionObject(
        @Part file: MultipartBody.Part,
    ): Call<DetectionResponse>
}