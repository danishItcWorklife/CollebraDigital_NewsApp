package com.collebra.digital.newsapptask.ui.main

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.collebra.digital.newsapptask.R
import com.collebra.digital.newsapptask.data.model.NewsArticle
import com.collebra.digital.newsapptask.state.DataState
import com.collebra.digital.newsapptask.ui.theme.NewsAppTaskTheme
import com.collebra.digital.newsapptask.utils.Constants
import com.collebra.digital.newsapptask.utils.Utilities
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import java.util.concurrent.Executor

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var executor: Executor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTaskTheme {
                val mainViewModel: MainViewModel = viewModel()
                val newsState by mainViewModel.newsResponse.collectAsState()
                val errorMessage by mainViewModel.errorMessage.collectAsState()
                val categories = Utilities.getStringArray(this, R.array.news_categories)
                var selectedCategory by remember { mutableStateOf(categories.first()) }
                NewsScreen(
                    newsState = newsState,
                    errorMessage = errorMessage,
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { category ->
                        selectedCategory = category
                        mainViewModel.fetchNews(
                            Constants.CountryCode,
                            category.lowercase(Locale.getDefault())
                        )
                    }
                )
            }
        }


        executor = ContextCompat.getMainExecutor(this)
        setPrompt()
        if (Utilities.isBiometricHardWareAvailable(this)) {
//            binding.DeviceHasBiometricFeatures.text = Constants.AVAILABLE
//            binding.DeviceHasFingerPrint.text = Constants.TRUE
//
//            //Enable the button if the device has biometric hardware available
//            binding.authenticatefingerprintbutton.isEnabled = true

            initBiometricPrompt(
                Constants.BIOMETRIC_AUTHENTICATION,
                Constants.BIOMETRIC_AUTHENTICATION_SUBTITLE,
                Constants.BIOMETRIC_AUTHENTICATION_DESCRIPTION,
                false
            )
        } else {
//            binding.DeviceHasBiometricFeatures.text = Constants.UNAVAILABLE
//            binding.DeviceHasFingerPrint.text = Constants.FALSE
//            binding.authenticatefingerprintbutton.isEnabled = false

            //Fallback, use device password/pin
            if (Utilities.deviceHasPasswordPinLock(this)) {
//                binding.authenticatefingerprintbutton.isEnabled = true
//                binding.authenticatefingerprintbutton.text = Constants.AUTHENTICATE_OTHER

                initBiometricPrompt(
                    Constants.PASSWORD_PIN_AUTHENTICATION,
                    Constants.PASSWORD_PIN_AUTHENTICATION_SUBTITLE,
                    Constants.PASSWORD_PIN_AUTHENTICATION_DESCRIPTION,
                    true
                )
            }
        }
    }

    private fun setPrompt() {
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    finish()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    setContent {
                        NewsAppTaskTheme {
                            val mainViewModel: MainViewModel = viewModel()
                            val newsState by mainViewModel.newsResponse.collectAsState()
                            val errorMessage by mainViewModel.errorMessage.collectAsState()
                            val categories = Utilities.getStringArray(this@MainActivity, R.array.news_categories)
                            var selectedCategory by remember { mutableStateOf(categories.first()) }
                            NewsScreen(
                                newsState = newsState,
                                errorMessage = errorMessage,
                                categories = categories,
                                selectedCategory = selectedCategory,
                                onCategorySelected = { category ->
                                    selectedCategory = category
                                    mainViewModel.fetchNews(
                                        Constants.CountryCode,
                                        category.lowercase(Locale.getDefault())
                                    )
                                }
                            )
                        }
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    finish()
                }
            })
    }

    private fun initBiometricPrompt(
        title: String,
        subtitle: String,
        description: String,
        setDeviceCred: Boolean
    ) {
        if (setDeviceCred) {
            /*For API level > 30
              Newer API setAllowedAuthenticators is used*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val authFlag =
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL or BiometricManager.Authenticators.BIOMETRIC_STRONG
                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setDescription(description)
                    .setAllowedAuthenticators(authFlag)
                    .build()
            } else {
                /*SetDeviceCredentials method deprecation is ignored here
                  as this block is for API level<30*/
                @Suppress("DEPRECATION")
                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setDescription(description)
                    .setDeviceCredentialAllowed(true)
                    .build()
            }
        } else {
            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setNegativeButtonText("Cancel")
                .build()
        }

        biometricPrompt.authenticate(promptInfo)

    }
}


@Composable
fun NewsScreen(
    newsState: DataState<List<NewsArticle>>,
    errorMessage: String,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Scrollable row for categories
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp) // Fixed height for the category row
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    content = {
                        items(categories) { category ->
                            CategoryItem(
                                category = category,
                                isSelected = category == selectedCategory,
                                onClick = { onCategorySelected(category) }
                            )
                        }
                    }
                )

                // Scrollable column for news articles
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    when (newsState) {
                        is DataState.Loading -> {
                            item {
                                Text(text = "Loading...")
                            }
                        }

                        is DataState.Success -> {
                            newsState.data?.let { articles ->
                                items(articles) { article ->
                                    NewsArticleItem(article = article)
                                }
                            }
                        }

                        is DataState.Error -> {
                            item {
                                Text(text = "Error: ${newsState.message}")
                            }
                        }

                        is DataState.Empty -> {
                            item {
                                Text(text = "No news available")
                            }
                        }
                    }

                    if (errorMessage.isNotEmpty()) {
                        item {
                            Text(text = errorMessage)
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun CategoryItem(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Text(
        text = category,
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = contentColor
    )
}

@Composable
fun NewsArticleItem(article: NewsArticle) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Title and description
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = article.title ?: "No Title",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = article.description ?: "No Description",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}
