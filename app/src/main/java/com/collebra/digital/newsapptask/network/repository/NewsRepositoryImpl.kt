package com.collebra.digital.newsapptask.network.repository


import com.collebra.digital.newsapptask.data.local.NewsDao
import com.collebra.digital.newsapptask.data.model.NewsArticle
import com.collebra.digital.newsapptask.network.api.ApiHelper
import com.collebra.digital.newsapptask.state.DataState
import com.collebra.digital.newsapptask.utils.NetworkHelper
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: ApiHelper,
    private val networkUtil: NetworkHelper,
    private val localDataSource: NewsDao
) : NewsRepository {
    override suspend fun getNews(
        countryCode: String, category: String,
        pageNumber: Int
    ): DataState<List<NewsArticle>> {
        return if (networkUtil.isNetworkConnected()) {
            // When the network is available, fetch data from the remote data source
            try {
                val response = remoteDataSource.getNews(countryCode, category, pageNumber)
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    if (result.status == "ok") {
                        // Saving news articles to the local database for offline access
                        result.articles.forEach { article ->
                            article.category = category
                            localDataSource.upsert(article)
                        }
                        DataState.Success(result.articles)
                    } else {
                        DataState.Error("An error occurred")
                    }
                } else {
                    DataState.Error("An error occurred")
                }
            } catch (e: Exception) {
                DataState.Error("Error occurred ${e.localizedMessage}")
            }
        } else {
            // When the network is not available, fetch data from the local data source
            try {
                val cachedNews = localDataSource.getNewsByCategory(category)
                if (cachedNews.isNotEmpty()) {
                    DataState.Success(cachedNews)
                } else {
                    DataState.Error("No cached news available")
                }
            } catch (e: Exception) {
                DataState.Error("Error occurred ${e.localizedMessage}")
            }
        }
    }

}