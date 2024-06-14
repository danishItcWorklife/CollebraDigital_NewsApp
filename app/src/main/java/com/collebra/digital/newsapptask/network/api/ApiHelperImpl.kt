package com.collebra.digital.newsapptask.network.api

import com.collebra.digital.newsapptask.data.model.NewsResponse
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val newsApi: NewsApi) : ApiHelper {

    override suspend fun getNews(countryCode: String, pageNumber: Int): Response<NewsResponse> =
        newsApi.getNews(countryCode, pageNumber)

    override suspend fun searchNews(query: String, pageNumber: Int): Response<NewsResponse> =
        newsApi.searchNews(query, pageNumber)

}