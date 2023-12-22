package com.example.newsapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.data.model.UserRequest
import com.example.newsapp.data.model.UserResponse
import com.example.newsapp.data.remote.apiKey
import com.example.newsapp.data.remote.userApiService
import com.example.newsapp.databinding.ActivityRegisterBinding
import com.example.newsapp.ui.MainActivity
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    companion object {
        var refreshToken: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRegisterAction()
    }

    private fun initRegisterAction() {
        binding.btnSignup.setOnClickListener {
            register()
        }

        binding.tvLogin.setOnClickListener {
            login()
        }
    }

    private fun register() {
        val username = binding.registerUname.text.toString().trim()
        val email = binding.registerEmail.text.toString().trim()
        val password = binding.registerPassword.text.toString().trim()
        val confirmPassword = binding.registerConfirmPassword.text.toString().trim()


        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userRequest = UserRequest()
        userRequest.username = username
        userRequest.email = email
        userRequest.password = password
        userRequest.confirmPassword = confirmPassword

        userApiService.register(userRequest, apiKey).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    Log.d("Email", users?.data?.email.toString())
                    Log.d("Username", users?.data?.username.toString())
                    Log.d("Token", users?.data?.refreshToken.toString())
                    LoginActivity.refreshToken = users?.data?.refreshToken

                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = parseErrorMessage(errorBody)
                    Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())
                Toast.makeText(this@RegisterActivity, "Registration failed. Please try again later.", Toast.LENGTH_SHORT).show()
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

    private fun login() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)

        finish()
    }
}
