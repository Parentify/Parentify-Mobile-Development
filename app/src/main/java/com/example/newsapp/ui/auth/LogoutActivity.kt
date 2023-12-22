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
import com.example.newsapp.databinding.ActivityLogoutBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLogoutAction()
    }

    private fun initLogoutAction() {
        binding.btnLogout.setOnClickListener {
            logout(isFromLogin = true) // Tandai bahwa logout berasal dari LoginActivity
        }
    }

    private fun logout(isFromLogin: Boolean) {
        val refreshTokenRequest = UserRequest()

        // Memilih refreshToken berdasarkan kondisi isFromLogin
        refreshTokenRequest.refreshTokens = if (isFromLogin) {
            LoginActivity.refreshToken
        } else {
            RegisterActivity.refreshToken
        }

        userApiService.logout(refreshTokenRequest, apiKey).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val users = response.body()
                Log.d("Email", users?.data?.email.toString())
                Log.d("Username", users?.data?.username.toString())

                // Lakukan pengalihan ke layar login setelah berhasil logout
                val intent = Intent(this@LogoutActivity, LoginActivity::class.java)
                startActivity(intent)

                finish()
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())
                // Handle kesalahan logout di sini
                // Misalnya, menampilkan pesan kesalahan kepada pengguna
                Toast.makeText(this@LogoutActivity, "Logout failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
