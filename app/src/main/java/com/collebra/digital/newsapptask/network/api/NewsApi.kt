package com.collebra.digital.newsapptask.network.api

import com.collebra.digital.newsapptask.data.model.NewsResponse
import com.collebra.digital.newsapptask.utils.Constants.Companion.API_KEY
import com.collebra.digital.newsapptask.utils.Constants.Companion.Category
import com.collebra.digital.newsapptask.utils.Constants.Companion.CountryCode
import com.collebra.digital.newsapptask.utils.Constants.Companion.DEFAULT_PAGE_INDEX
import com.collebra.digital.newsapptask.utils.Constants.Companion.QUERY_PER_PAGE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("country")
        countryCode: String = CountryCode,
        @Query("category")
        category: String = Category,
        @Query("page")
        pageNumber: Int = DEFAULT_PAGE_INDEX,
        @Query("pageSize")
        pageSize: Int = QUERY_PER_PAGE,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>
}