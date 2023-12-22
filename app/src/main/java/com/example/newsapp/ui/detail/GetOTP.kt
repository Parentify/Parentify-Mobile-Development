package com.example.newsapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.databinding.ActivityGetOtpBinding
import com.example.newsapp.ui.auth.ForgotActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class GetOTP : AppCompatActivity() {
    private lateinit var binding: ActivityGetOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetOtpBinding.inflate(layoutInflater)

        setContentView(binding.root) // Update this line

        initGetOTPAction()
    }

    private fun initGetOTPAction() {
        binding.btnGetOtp.setOnClickListener {
            getOtp()
        }

        binding.btnOtpBack.setOnClickListener {
            back()
        }
    }

    private fun back() {
        val intent = Intent(this@GetOTP, ForgotActivity::class.java)
        startActivity(intent)

        finish()
    }

    private fun getOtp() {
        val userInput = binding.etNoTelp.text.toString().trim()

        if (userInput.isEmpty()) {
            // Menampilkan pesan kesalahan jika kosong
            Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show()
            return
        }

        val phoneNumber = "+$userInput" // Format nomor telepon dengan kode negara, misalnya, "+1234567890"

        // Metode untuk mengirim OTP
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60, // Waktu timeout dalam detik
            TimeUnit.SECONDS,
            this, // Activity atau context
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Otomatis terverifikasi, gunakan credential jika perlu
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    // Gagal verifikasi, tangani kesalahan di sini
                    Toast.makeText(this@GetOTP, "Failed to send OTP: ${exception.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    // Kode OTP telah dikirim, tangani verifikasi di sini
                    // Simpan verificationId dan token jika diperlukan untuk verifikasi lebih lanjut
                    val intent = Intent(this@GetOTP, VerifyOTP::class.java)
                    intent.putExtra("verificationId", verificationId)
                    startActivity(intent)
                }
            }
        )
    }

}
