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
import com.example.newsapp.databinding.ActivityLoginBinding
import com.example.newsapp.ui.MainActivity
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding // Update this line

    companion object {
        var refreshToken: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater) // Update this line
        setContentView(binding.root) // Update this line

        initLoginAction()
    }

    private fun initLoginAction() {
        binding.btnLogin.setOnClickListener { // Update this line
            login()
        }

        binding.tvForgotPassword.setOnClickListener {
            forgot()
        }

        binding.tvRegister.setOnClickListener {
            register()
        }
    }

    private fun login() {
        val userInput = binding.etLoginEmail.text.toString().trim()

        // Memeriksa apakah userInput kosong
        if (userInput.isEmpty()) {
            // Menampilkan pesan kesalahan jika kosong
            Toast.makeText(this, "Please enter an email or username", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if userInput is an email address
        val isEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(userInput).matches()

        val userRequest = UserRequest()

        // Jika userInput adalah alamat email, set email pada userRequest
        // Jika bukan, set username pada userRequest
        if (isEmail) {
            userRequest.email = userInput
        } else {
            userRequest.username = userInput
        }

        userRequest.password = binding.etLoginPassword.text.toString().trim()

        userApiService.login(userRequest, apiKey).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    Log.d("Email", users?.data?.email.toString())
                    Log.d("Username", users?.data?.username.toString())
                    Log.d("Token", users?.data?.refreshToken.toString())
                    refreshToken = users?.data?.refreshToken

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)

                    finish()
                } else {
                    // Handle unsuccessful response and display API error message
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = parseErrorMessage(errorBody)
                    Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())

                // Display a Toast message for network-related failures
                val errorMessage = if (t.message.isNullOrEmpty()) {
                    "Network error. Please check your internet connection."
                } else {
                    t.message.toString()
                }

                Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun parseErrorMessage(errorBody: String?): String {
        return try {
            // Parse JSON to extract error message
            val json = JsonParser.parseString(errorBody).asJsonObject
            json.getAsJsonPrimitive("Message").asString
        } catch (e: Exception) {
            // Return the original error body if parsing fails
            errorBody ?: "Unknown error"
        }
    }

    private fun register() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)

        finish()
    }

    private fun forgot() {
        val intent = Intent(this@LoginActivity, ForgotActivity::class.java)
        startActivity(intent)

        finish()
    }
}
