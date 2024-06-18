package com.collebra.digital.newsapptask.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.collebra.digital.newsapptask.data.model.NewsArticle
import com.collebra.digital.newsapptask.di.CoroutinesDispatcherProvider
import com.collebra.digital.newsapptask.network.repository.NewsRepository
import com.collebra.digital.newsapptask.state.DataState
import com.collebra.digital.newsapptask.utils.Constants
import com.collebra.digital.newsapptask.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val networkHelper: NetworkHelper,
    private val coroutinesDispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {

    private val TAG = "MainViewModel"
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    private val _newsResponse = MutableStateFlow<DataState<List<NewsArticle>>>(DataState.Empty())
    val newsResponse: StateFlow<DataState<List<NewsArticle>>> get() = _newsResponse


    init {
        fetchNews(Constants.CountryCode, Constants.Category)
    }

    fun fetchNews(countryCode: String, category: String) {
        viewModelScope.launch(coroutinesDispatcherProvider.io) {
            _newsResponse.value = DataState.Loading()
            val response = repository.getNews(
                countryCode,
                category,
                Constants.DEFAULT_PAGE_INDEX
            ) // Assuming you always start with page 1
            when (response) {
                is DataState.Success -> {
                    _newsResponse.value = handleFeedNewsResponse(response)
                 }

                is DataState.Error -> {
                    _newsResponse.value = DataState.Error(response.message ?: "Error")
                }

                else -> {
                    // Handle other cases if needed
                }
            }
        }
    }

//    private fun handleFeedNewsResponse(response: NetworkState<List<NewsArticle>>): NetworkState<List<NewsArticle>> {
//        response.data?.let { resultResponse ->
//            return NetworkState.Success(resultResponse)
//        } ?: run {
//            return NetworkState.Error("No data found")
//        }
//    }
private fun handleFeedNewsResponse(response: DataState<List<NewsArticle>>): DataState<List<NewsArticle>> {
    // No need to cast response.data again, just return response as-is
    return response
}

}
