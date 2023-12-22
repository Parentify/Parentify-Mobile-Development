package com.example.newsapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "articles_data")
data class Article(
    @ColumnInfo(name = "articleId")
    @PrimaryKey(autoGenerate = true)
    val articleId: Int?,

    @ColumnInfo(name = "author")
    @SerializedName("author")
    val author: String?,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String?,

    @ColumnInfo(name = "description")
    @SerializedName("description")
    val description: String?,

    @ColumnInfo(name = "url")
    @SerializedName("url")
    val url: String,

    @ColumnInfo(name = "urlToImage")
    @SerializedName("urlToImage")
    val urlToImage: String,

    @ColumnInfo(name = "publishedAt")
    @SerializedName("publishedAt")
    val publishedAt: String?,

    @ColumnInfo(name = "source")
    @SerializedName("source")
    val source: Source?,

    @ColumnInfo(name = "content")
    @SerializedName("content")
    val content: String?
) : Serializable