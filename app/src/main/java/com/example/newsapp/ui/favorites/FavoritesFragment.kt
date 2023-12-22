package com.example.newsapp.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.newsapp.data.model.UserRequest
import com.example.newsapp.data.model.UserResponse
import com.example.newsapp.data.remote.apiKey
import com.example.newsapp.data.remote.userApiService
import com.example.newsapp.databinding.FragmentFavoritesBinding
import com.example.newsapp.ui.auth.LoginActivity
import com.example.newsapp.ui.auth.LogoutActivity
import com.example.newsapp.ui.auth.RegisterActivity
import com.example.newsapp.ui.detail.DetailFAQ
import com.example.newsapp.ui.detail.DetailParentify
import com.example.newsapp.ui.detail.DetailSetting
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        initProfileAction()
        return binding.root
    }

    private fun initProfileAction() {
        binding.llLogout.setOnClickListener {
            logout(isFromLogin = true) // Tandai bahwa logout berasal dari LoginActivity
        }

        binding.llSetting.setOnClickListener {
            setting()
        }

        binding.llFaq.setOnClickListener {
            faq()
        }

        binding.llAboutApk.setOnClickListener {
            about()
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
                val intent = Intent(requireContext(), LogoutActivity::class.java)
                startActivity(intent)

                // If this code is in a Fragment, you should not call finish()
                // finish()
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("Error", t.message.toString())
                // Handle kesalahan logout di sini
                // Misalnya, menampilkan pesan kesalahan kepada pengguna
                Toast.makeText(requireContext(), "Logout failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setting() {
        val intent = Intent(requireContext(), DetailSetting::class.java)
        startActivity(intent)
    }

    private fun faq() {
        val intent = Intent(requireContext(), DetailFAQ::class.java)
        startActivity(intent)
    }

    private fun about() {
        val intent = Intent(requireContext(), DetailParentify::class.java)
        startActivity(intent)
    }
}