package com.example.newsapp.data.model.detection

import com.google.gson.annotations.SerializedName

data class DetectionResponse(
    @SerializedName("prediction")
    val prediction:String,
    @SerializedName("confidence")
    val confidence: String
)
