package com.example.newsapp.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserResponse {
    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data{
        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("username")
        @Expose
        var username: String? = null

        @SerializedName("refreshToken")
        @Expose
        var refreshToken: String? = null

        @SerializedName("password")
        @Expose
        var password: String? = null

        @SerializedName("newPassword")
        @Expose
        var newPassword: String? = null

        @SerializedName("confirmPassword")
        @Expose
        var confirmPassword: String? = null

        @SerializedName("oldPassword")
        @Expose
        var oldPassword: String? = null
    }
}
