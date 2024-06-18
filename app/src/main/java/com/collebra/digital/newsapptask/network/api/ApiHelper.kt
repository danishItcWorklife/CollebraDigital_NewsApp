package com.collebra.digital.newsapptask.network.api

import com.collebra.digital.newsapptask.data.model.NewsResponse
import retrofit2.Response

interface ApiHelper {
     suspend fun getNews(countryCode: String,category: String, pageNumber: Int): Response<NewsResponse>
}