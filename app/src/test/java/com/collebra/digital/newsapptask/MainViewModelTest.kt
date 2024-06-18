import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.collebra.digital.newsapptask.network.api.NewsApi
import com.collebra.digital.newsapptask.network.repository.NewsRepositoryImpl
import com.collebra.digital.newsapptask.state.DataState
import com.collebra.digital.newsapptask.ui.main.MainViewModel
import com.collebra.digital.newsapptask.utils.Constants.Companion.Category
import com.collebra.digital.newsapptask.utils.Constants.Companion.CountryCode
import com.collebra.digital.newsapptask.utils.Constants.Companion.DEFAULT_PAGE_INDEX
import com.collebra.digital.newsapptask.utils.NetworkHelper
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MainViewModelTest {
    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var newsApi: NewsApi

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var newsRepositoryImpl: NewsRepositoryImpl

    private val testDispatcher = coroutineRule.testDispatcher

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = MainViewModel(
            repository = newsRepositoryImpl,
            networkHelper = networkHelper,
            coroutinesDispatcherProvider = provideFakeCoroutinesDispatcherProvider(testDispatcher)
        )
    }

    @Test
    fun `when calling for results then return loading state`() {
        coroutineRule.runBlockingTest {
            whenever(networkHelper.isNetworkConnected())
                .thenReturn(true)
            whenever(newsRepositoryImpl.getNews(CountryCode, Category, DEFAULT_PAGE_INDEX))
                .thenReturn(DataState.Loading())

            //When
            viewModel.fetchNews(CountryCode, Category)

            //Then
            assertThat(viewModel.newsResponse.value).isNotNull()
            assertThat(viewModel.newsResponse.value.data).isNull()
            assertThat(viewModel.newsResponse.value.message).isNull()
        }
    }

    @Test
    fun `when calling for results then return news results`() {
        coroutineRule.runBlockingTest {
            // Mock network connectivity
            whenever(networkHelper.isNetworkConnected()).thenReturn(true)

            // Mock repository response with a success state containing fake news articles
            val fakeArticles = FakeDataUtil.getFakeArticles().toList()
            val successResponse = DataState.Success(fakeArticles)
            whenever(newsRepositoryImpl.getNews(CountryCode, Category, DEFAULT_PAGE_INDEX))
                .thenReturn(successResponse)

            // Call fetchNews
            viewModel.fetchNews(CountryCode, Category)

            // Assert on the ViewModel's state
            assertThat(viewModel.newsResponse.value).isInstanceOf(DataState::class.java)
            val response = viewModel.newsResponse.value
            assertThat(response).isInstanceOf(DataState.Success::class.java)
            assertThat(response.data).isNotNull()
            assertThat(response.data).hasSize(fakeArticles.size)
            assertThat(response.data).containsExactlyElementsIn(fakeArticles).inOrder()
        }
    }

    @Test
    fun `when calling for results then return error`() {
        coroutineRule.runBlockingTest {
            whenever(networkHelper.isNetworkConnected())
                .thenReturn(true)
            // Stub repository with fake favorites
            whenever(newsRepositoryImpl.getNews(CountryCode, Category, DEFAULT_PAGE_INDEX))
                .thenAnswer { DataState.Error("Error occurred", null) }

            //When
            viewModel.fetchNews(CountryCode, Category)

            //then
            val response = viewModel.newsResponse.value
            assertThat(response.message).isNotNull()
            assertThat(response.message).isEqualTo("Error occurred")
        }
    }

    @After
    fun release() {
        Mockito.framework().clearInlineMocks()
    }
}