package com.collebra.digital.newsapptask.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.collebra.digital.newsapptask.data.model.NewsArticle
import com.collebra.digital.newsapptask.data.model.NewsResponse
import com.collebra.digital.newsapptask.state.NetworkState
import com.collebra.digital.newsapptask.ui.theme.NewsAppTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTaskTheme {
                val mainViewModel: MainViewModel = viewModel()
                val newsState by mainViewModel.newsResponse.collectAsState()
                val errorMessage by mainViewModel.errorMessage.collectAsState()
                NewsScreen(newsState, errorMessage)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    newsState: NetworkState<NewsResponse>,
    errorMessage: String,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "News") })
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (newsState) {
                    is NetworkState.Loading -> {

                    }
                    is NetworkState.Success -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            items(newsState.data!!.articles.size) { index ->
                                val article = newsState.data!!.articles[index]
                                NewsArticleItem(article = article)
                                Divider() // Optional: Adds a divider between items
                            }
                        }
                    }
                    is NetworkState.Error -> {
                        Text(
                            text = "Error: ${newsState.message}",
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                        )
                    }
                    is NetworkState.Empty -> {
                        Text(
                            text = "No news available",
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                        )
                    }
                }
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }
            }
        }
    )
}

@Composable
fun NewsArticleItem(article: NewsArticle) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        article.title?.let {
            Text(
                text = it,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        article.description?.let {
            Text(
                text = it,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        // Add more details or customize the layout as per your article model
    }
}

