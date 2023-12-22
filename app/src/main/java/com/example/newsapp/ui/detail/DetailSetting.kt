package com.example.newsapp.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.newsapp.data.model.UserRequest
import com.example.newsapp.data.model.UserResponse
import com.example.newsapp.data.remote.apiKey
import com.example.newsapp.data.remote.userApiService
import com.example.newsapp.databinding.ActivityDetailSettingBinding
import com.example.newsapp.ui.MainActivity
import com.example.newsapp.ui.auth.LoginActivity
import com.example.newsapp.ui.auth.RegisterActivity
import com.example.newsapp.ui.favorites.FavoritesFragment
import com.google.gson.JsonParser

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailSetting : AppCompatActivity() {
    private lateinit var binding: ActivityDetailSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSettingAction()
    }

    private fun initSettingAction(){
        binding.btnProfileBack.setOnClickListener {
            back()
        }

        binding.btnProfileSave.setOnClickListener {
            save(isFromLogin = true)
        }
    }

    private fun back() {
        val intent = Intent(this@DetailSetting, FavoritesFragment::class.java)
        startActivity(intent)

        finish()
    }

    private fun save(isFromLogin: Boolean) {
        val refreshTokenRequest = UserRequest()

        // Memilih refreshToken berdasarkan kondisi isFromLogin
        refreshTokenRequest.refreshTokens = if (isFromLogin) {
            LoginActivity.refreshToken
        } else {
            RegisterActivity.refreshToken
        }

        val newUsername = binding.etEditUname.text.toString().trim()
        val newEmail = binding.etEditEmail.text.toString().trim()
        val oldPassword = binding.etEditOldPass.text.toString().trim()
        val newPassword = binding.etEditPass.text.toString().trim()

        if (newUsername.isEmpty() || newEmail.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userRequest = UserRequest()
        userRequest.newUsername = newUsername
        userRequest.newEmail = newEmail
        userRequest.oldPassword = oldPassword
        userRequest.newPassword = newPassword

        userApiService.updateUser(userRequest, apiKey).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    Log.d("Email", users?.data?.email.toString())
                    Log.d("Username", users?.data?.username.toString())
                    Log.d("Token", users?.data?.refreshToken.toString())

                    LoginActivity.refreshToken = users?.data?.refreshToken

                    val intent = Intent(this@DetailSetting, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = parseErrorMessage(errorBody)
                    Toast.makeText(this@DetailSetting, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())
                Toast.makeText(this@DetailSetting, "Registration failed. Please try again later.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun parseErrorMessage(errorBody: String?): String {
        return try {
            val json = JsonParser.parseString(errorBody).asJsonObject
            json.getAsJsonPrimitive("Message").asString
        } catch (e: Exception) {
            errorBody ?: "Unknown error"
        }
    }
}