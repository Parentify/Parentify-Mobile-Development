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
import com.example.newsapp.databinding.ActivityForgotPasswordBinding
import com.google.gson.JsonParser

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)

        setContentView(binding.root) // Update this line

        initForgotAction()
    }

    private fun initForgotAction(){
        binding.btnForgotBack.setOnClickListener {
            back()
        }

        binding.btnReset.setOnClickListener {
            reset()
        }
    }

    private fun back() {
        val intent = Intent(this@ForgotActivity, LoginActivity::class.java)
        startActivity(intent)

        finish()
    }

    private fun reset() {
        val userInput = binding.etForgotUname.text.toString().trim()

        // Memeriksa apakah userInput kosong
        if (userInput.isEmpty()) {
            // Menampilkan pesan kesalahan jika kosong
            Toast.makeText(this, "Please fill the box", Toast.LENGTH_SHORT).show()
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

        userRequest.newPassword = binding.etForgotSandi.text.toString().trim()
        userRequest.confirmPassword = binding.etConfirmSandi.text.toString().trim()

        userApiService.forgotVerify(userRequest, apiKey).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    // Pindah ke halaman verifikasi OTP
                    val intent = Intent(this@ForgotActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Handle unsuccessful response and display API error message
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = parseErrorMessage(errorBody)
                    Toast.makeText(this@ForgotActivity, errorMessage, Toast.LENGTH_SHORT).show()
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

                Toast.makeText(this@ForgotActivity, errorMessage, Toast.LENGTH_SHORT).show()
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
}
