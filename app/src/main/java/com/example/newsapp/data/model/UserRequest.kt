package com.example.newsapp.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserRequest {
    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("newUsername")
    @Expose
    var newUsername: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("newEmail")
    @Expose
    var newEmail: String? = null

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

    @SerializedName("refreshToken")
    @Expose
    var refreshTokens: String? = null
}