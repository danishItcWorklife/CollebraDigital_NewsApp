package com.collebra.digital.newsapptask.data.model


data class NewsResponse(
    val articles: MutableList<NewsArticle>,
    val status: String,
    val totalResults: Int
)