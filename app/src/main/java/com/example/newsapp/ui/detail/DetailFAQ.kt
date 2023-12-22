package com.example.newsapp.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newsapp.databinding.ActivityDetailFaqBinding
import com.example.newsapp.ui.favorites.FavoritesFragment

class DetailFAQ : AppCompatActivity() {

    private lateinit var binding: ActivityDetailFaqBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFaqAction()
    }

    private fun initFaqAction(){
        binding.btnFaqBack.setOnClickListener {
            back()
        }
    }

    private fun back() {
        val intent = Intent(this@DetailFAQ, FavoritesFragment::class.java)
        startActivity(intent)

        finish()
    }
}