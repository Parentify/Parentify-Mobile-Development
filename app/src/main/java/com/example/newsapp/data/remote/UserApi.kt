package com.example.newsapp.data.remote

import com.example.newsapp.data.local.Retro
import com.example.newsapp.data.model.UserRequest
import com.example.newsapp.data.model.UserResponse
import com.example.newsapp.data.model.detection.DetectionResponse
import com.example.newsapp.data.model.food.FoodResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

const val apiKey = "12eba900e5501b366d5e3ca924de4cd59fc0760eb5e4927855de945e88858da49a783f48c3b28150"
val userApiService = Retro().createApiService()
val foodApiService = Retro().createFoodService()

interface UserApi {
    @HTTP(method = "POST", path = "auth/login", hasBody = true)
    fun login(
        @Body userRequest: UserRequest,
        @Query("api_key") apiKey: String
    ): Call<UserResponse>

    @HTTP(method = "POST", path = "auth/register", hasBody = true)
    fun register(
        @Body userRequest: UserRequest,
        @Query("api_key") apiKey: String
    ): Call<UserResponse>

    @HTTP(method = "PUT", path = "auth/edit/user", hasBody = true)
    fun updateUser(
        @Body userRequest: UserRequest,
        @Query("api_key") apiKey: String
    ): Call<UserResponse>

    @HTTP(method = "PATCH", path = "/auth/edit/password", hasBody = true)
    fun forgotVerify(
        @Body userRequest: UserRequest,
        @Query("api_key") apiKey: String
    ): Call<UserResponse>

    @HTTP(method = "DELETE", path = "auth/logout", hasBody = true)
    fun logout(
        @Body refreshTokenRequest: UserRequest,
        @Query("api_key") apiKey: String
    ): Call<UserResponse>
}

interface FoodApi{
    @HTTP(method = "POST", path = "food/addFood", hasBody = true)
    fun addFood(
        @Body userRequest: UserRequest,
        @Query("api_key") apiKey: String
    ): Call<UserResponse>

    @HTTP(method = "GET", path = "food/getClasification", hasBody = false)
    fun getFoodDetail(
        @Query("food_name") foodName: String,
        @Query("api_key") apiKey: String
    ): Call<List<FoodResponse>>
}
