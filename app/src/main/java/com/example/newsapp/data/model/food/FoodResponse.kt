package com.example.newsapp.data.model.food

import com.google.gson.annotations.SerializedName

data class FoodResponse(
    @SerializedName("img")
    val img: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("nutrition")
    val nutrition: String,
    @SerializedName("data")
    val data: List<ListInformation>
)

data class ListInformation(
    @SerializedName("information")
    val information: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("texture")
    val texture: String,
){
    val textureString: String
        get() = "Tekstur : $texture"
}

