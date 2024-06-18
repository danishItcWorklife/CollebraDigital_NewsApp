package com.collebra.digital.newsapptask

import FakeDataUtil
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.collebra.digital.newsapptask.data.local.NewsDao
import com.collebra.digital.newsapptask.data.model.NewsArticle
import com.collebra.digital.newsapptask.data.model.NewsResponse
import com.collebra.digital.newsapptask.data.model.Source
import com.collebra.digital.newsapptask.network.api.ApiHelper
import com.collebra.digital.newsapptask.network.repository.NewsRepositoryImpl
import com.collebra.digital.newsapptask.state.DataState
import com.collebra.digital.newsapptask.utils.NetworkHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class NewsRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()

    private lateinit var newsRepositoryImpl: NewsRepositoryImpl
    private val remoteDataSource: ApiHelper = mockk()
    private val networkUtil: NetworkHelper = mockk()
    private val localDataSource: NewsDao = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        newsRepositoryImpl = NewsRepositoryImpl(remoteDataSource, networkUtil, localDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getNews network available success response`() = runTest {
        // Mock network status
        coEvery { networkUtil.isNetworkConnected() } returns true

        // Mock API response
        val articles = FakeDataUtil.getFakeArticles()
        val response = NewsResponse(status = "ok", articles = articles, totalResults = 20)
        coEvery { remoteDataSource.getNews(any(), any(), any()) } returns Response.success(response)

        // Mock local database operation
        coEvery { localDataSource.upsert(any()) } returns Unit

        // Call the method
        val result = newsRepositoryImpl.getNews("us", "business", 1)

        // Verify interactions and result
        coVerify { remoteDataSource.getNews("us", "business", 1) }
        coVerify { localDataSource.upsert(any()) }
        assert(result is DataState.Success && result.data == articles)
    }

    @Test
    fun `getNews network available error response`() = runTest {
        // Mock network status
        coEvery { networkUtil.isNetworkConnected() } returns true

        // Mock API response
        coEvery { remoteDataSource.getNews(any(), any(), any()) } returns Response.error(
            400,
            mockk(relaxed = true)
        )

        // Call the method
        val result = newsRepositoryImpl.getNews("us", "business", 1)

        // Verify interactions and result
        coVerify { remoteDataSource.getNews("us", "business", 1) }
        assert(result is DataState.Error)
    }

    @Test
    fun `getNews network unavailable cached news available`() = runTest {
        // Mock network status
        coEvery { networkUtil.isNetworkConnected() } returns false

        // Mock local database operation
        val cachedArticles = listOf(
            NewsArticle(
                title = "Cached Article",
                author = "Author",
                description = "Description",
                url = "http://test.url",
                urlToImage = "http://test.url/image.jpg",
                publishedAt = "2024-06-16T15:03:00Z",
                content = "Content",
                category = "business",
                source = Source(
                    id = 1, name = "BBC"
                )
            )
        )
        coEvery { localDataSource.getNewsByCategory(any()) } returns cachedArticles

        // Call the method
        val result = newsRepositoryImpl.getNews("us", "business", 1)

        // Verify interactions and result
        coVerify { localDataSource.getNewsByCategory("business") }
        assert(result is DataState.Success && result.data == cachedArticles)
    }

    @Test
    fun `getNews network unavailable no cached news`() = runTest {
        // Mock network status
        coEvery { networkUtil.isNetworkConnected() } returns false

        // Mock local database operation
        coEvery { localDataSource.getNewsByCategory(any()) } returns emptyList()

        // Call the method
        val result = newsRepositoryImpl.getNews("us", "business", 1)

        // Verify interactions and result
        coVerify { localDataSource.getNewsByCategory("business") }
        assert(result is DataState.Error)
    }
}
