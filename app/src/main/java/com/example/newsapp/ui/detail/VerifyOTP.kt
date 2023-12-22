package com.example.newsapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newsapp.databinding.ActivityVerifyOtpBinding


class VerifyOTP : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVerifyAction()
    }

    private fun initVerifyAction() {
        binding.btnVerifiyOtp.setOnClickListener {
            forgotVerify() // Tandai bahwa logout berasal dari LoginActivity
        }
    }

    private fun forgotVerify() {

    }

}