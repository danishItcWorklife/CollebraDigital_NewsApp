package com.collebra.digital.newsapptask.di


import android.content.Context
import com.collebra.digital.newsapptask.data.local.NewsDao
import com.collebra.digital.newsapptask.data.local.NewsDatabase
import com.collebra.digital.newsapptask.network.api.ApiHelper
import com.collebra.digital.newsapptask.network.repository.NewsRepository
import com.collebra.digital.newsapptask.network.repository.NewsRepositoryImpl
import com.collebra.digital.newsapptask.utils.NetworkHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        NewsDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideNewsDao(db: NewsDatabase) = db.getNewsDao()

    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: ApiHelper,
        localDataSource: NewsDao,
        networkHelper: NetworkHelper,
    ) = NewsRepositoryImpl(remoteDataSource, networkHelper, localDataSource)

    @Singleton
    @Provides
    fun provideINewsRepository(repository: NewsRepositoryImpl): NewsRepository = repository
}