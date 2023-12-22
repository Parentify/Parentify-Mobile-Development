package com.example.newsapp.di

import android.content.Context
import androidx.room.Room
import com.example.newsapp.data.local.ArticleDao
import com.example.newsapp.data.local.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideLocalDatabase(
        @ApplicationContext context: Context,
    ): ArticleDatabase =
        Room.databaseBuilder(
            context,
            ArticleDatabase::class.java,
            "article_database"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideNoteDao(articleDatabase: ArticleDatabase): ArticleDao = articleDatabase.articleDao()
}