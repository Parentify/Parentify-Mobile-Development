package com.example.newsapp.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.newsapp.databinding.ActivityDetailParentifyBinding
import com.example.newsapp.ui.favorites.FavoritesFragment


class DetailParentify : AppCompatActivity() {

    private lateinit var binding: ActivityDetailParentifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailParentifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAboutAction()
    }

    private fun initAboutAction() {
        binding.btnAboutBack.setOnClickListener {
            back()
        }
    }

    private fun back() {
        val intent = Intent(this@DetailParentify, FavoritesFragment::class.java)
        startActivity(intent)

        finish()
    }
}