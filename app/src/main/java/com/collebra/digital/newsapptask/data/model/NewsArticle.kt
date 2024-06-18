package com.collebra.digital.newsapptask.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.collebra.digital.newsapptask.utils.Constants
import java.io.Serializable

@Entity(
    tableName = Constants.TB_NAME
)
data class NewsArticle(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    var publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    var category: String?="",
    ) : Serializable