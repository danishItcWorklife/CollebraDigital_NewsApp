package com.collebra.digital.newsapptask.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.collebra.digital.newsapptask.data.model.NewsArticle

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(newsArticle: NewsArticle): Long

    @Update
    suspend fun update(newsArticle: NewsArticle)

    @Query("SELECT * FROM news_articles WHERE url = :url LIMIT 1")
    suspend fun getArticleByUrl(url: String): NewsArticle?

    @Transaction
    suspend fun upsert(newsArticle: NewsArticle) {
        val existingArticle = getArticleByUrl(newsArticle.url ?: "")
        if (existingArticle == null) {
            insert(newsArticle)
        } else {
            update(newsArticle.copy(id = existingArticle.id))
        }
    }

    @Query("SELECT * FROM news_articles WHERE category = :category")
    suspend fun getNewsByCategory(category: String): List<NewsArticle>


}