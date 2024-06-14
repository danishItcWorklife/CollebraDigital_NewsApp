package com.collebra.digital.newsapptask.network.repository

import androidx.lifecycle.LiveData
import com.collebra.digital.newsapptask.data.model.NewsArticle
import com.collebra.digital.newsapptask.data.model.NewsResponse
import com.collebra.digital.newsapptask.state.NetworkState


interface INewsRepository {
    suspend fun getNews(countryCode: String, pageNumber: Int): NetworkState<NewsResponse>

    suspend fun searchNews(searchQuery: String, pageNumber: Int): NetworkState<NewsResponse>

    suspend fun saveNews(news: NewsArticle): Long

    fun getSavedNews(): LiveData<List<NewsArticle>>

    suspend fun deleteNews(news: NewsArticle)

    suspend fun deleteAllNews()
}
