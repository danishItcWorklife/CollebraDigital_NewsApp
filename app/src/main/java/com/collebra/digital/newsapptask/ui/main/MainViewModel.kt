package com.collebra.digital.newsapptask.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.collebra.digital.newsapptask.data.model.NewsArticle
import com.collebra.digital.newsapptask.data.model.NewsResponse
import com.collebra.digital.newsapptask.di.CoroutinesDispatcherProvider
import com.collebra.digital.newsapptask.network.repository.INewsRepository
import com.collebra.digital.newsapptask.state.NetworkState
import com.collebra.digital.newsapptask.utils.Constants
import com.collebra.digital.newsapptask.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: INewsRepository,
    private val networkHelper: NetworkHelper,
    private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {

    private val TAG = "MainViewModel"
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    private val _newsResponse = MutableStateFlow<NetworkState<NewsResponse>>(NetworkState.Empty())
    val newsResponse: StateFlow<NetworkState<NewsResponse>> get() = _newsResponse


    private var feedResponse: NewsResponse? = null
    var feedNewsPage = 1

    var totalPage = 1

    init {
        fetchNews(Constants.CountryCode)
    }

    fun fetchNews(countryCode: String) {
        if (feedNewsPage <= totalPage) {
            if (networkHelper.isNetworkConnected()) {
                viewModelScope.launch(coroutinesDispatcherProvider.io) {
                    _newsResponse.value = NetworkState.Loading()
                    when (val response = repository.getNews(countryCode, feedNewsPage)) {
                        is NetworkState.Success -> {
                            _newsResponse.value = handleFeedNewsResponse(response)
                        }
                        is NetworkState.Error -> {
                            _newsResponse.value = NetworkState.Error(response.message ?: "Error")
                        }
                        else -> {}
                    }
                }
            } else {
                _errorMessage.value = "No internet available"
            }
        }
    }

    private fun handleFeedNewsResponse(response: NetworkState<NewsResponse>): NetworkState<NewsResponse> {
        response.data?.let { resultResponse ->
            if (feedResponse == null) {
                feedNewsPage = 2
                feedResponse = resultResponse
            } else {
                feedNewsPage++
                val oldArticles = feedResponse?.articles
                val newArticles = resultResponse.articles
                oldArticles?.addAll(newArticles)
            }
            feedResponse?.let {
                convertPublishedDate(it)
            }
            return NetworkState.Success(feedResponse ?: resultResponse)
        }
        return NetworkState.Error("No data found")
    }



    private fun convertPublishedDate(currentResponse: NewsResponse) {
        currentResponse.articles.map { article ->
            article.publishedAt?.let {
                article.publishedAt = formatDate(it)
            }
        }
    }

    fun formatDate(strCurrentDate: String): String {
        var convertedDate = ""
        try {
            if (strCurrentDate.isNotEmpty() && strCurrentDate.contains("T")) {
                val local = Locale("US")
                var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", local)
                val newDate: Date? = format.parse(strCurrentDate)

                format = SimpleDateFormat("MMM dd, yyyy hh:mm a", local)
                newDate?.let {
                    convertedDate = format.format(it)
                }
            } else {
                convertedDate = strCurrentDate
            }
        } catch (e: Exception) {
            e.message?.let {
                Log.e(TAG, it)
            }
            convertedDate = strCurrentDate
        }
        return convertedDate
    }

    fun hideErrorToast() {
        _errorMessage.value = ""
    }


    private fun onError(throwable: Throwable) {
        throwable.message?.let {
            _errorMessage.value = it
        }
    }
}
