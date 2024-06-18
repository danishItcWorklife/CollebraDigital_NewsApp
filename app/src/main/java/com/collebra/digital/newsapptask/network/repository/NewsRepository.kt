package com.collebra.digital.newsapptask.network.repository

import com.collebra.digital.newsapptask.data.model.NewsArticle
import com.collebra.digital.newsapptask.state.DataState


interface NewsRepository {
    suspend fun getNews(
        countryCode: String,
        category: String,
        pageNumber: Int
    ): DataState<List<NewsArticle>>

}
