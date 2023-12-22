package com.example.newsapp.ui.detection.detail

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.data.model.food.FoodResponse
import com.example.newsapp.data.remote.apiKey
import com.example.newsapp.data.remote.foodApiService
import com.example.newsapp.databinding.ActivityDetailDetectionBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class DetailDetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDetectionBinding

    private var listAdapter: DetailDetectionAdapter = DetailDetectionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        val nameFood = intent.getStringExtra(ARGS_TITLE).toString()

        with(binding){
            back.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            rvItem.apply {
                adapter = listAdapter
            }

        }

        val stringWithoutSpaces = nameFood.replace(" ", "")

        getDetailFood(stringWithoutSpaces)

    }

    private fun getDetailFood(keyword: String){
        foodApiService.getFoodDetail(keyword, apiKey).enqueue(object : Callback<List<FoodResponse>>{
            override fun onResponse(
                call: Call<List<FoodResponse>>,
                response: Response<List<FoodResponse>>
            ) {
                val dataResponse = response.body()?.first()
                binding.dataItemBind = dataResponse

                listAdapter.submitList(dataResponse?.data)
                Picasso.get().load(dataResponse?.img).into(binding.ivFood)
            }

            override fun onFailure(call: Call<List<FoodResponse>>, t: Throwable) {
                Toast.makeText(this@DetailDetectionActivity, "Failed Get Data Food", Toast.LENGTH_SHORT).show()
            }

        })
    }

    companion object {
        const val ARGS_TITLE = "Name Food"
    }
}