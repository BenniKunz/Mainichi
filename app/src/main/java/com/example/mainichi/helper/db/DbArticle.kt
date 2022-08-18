package com.example.mainichi.helper.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mainichi.helper.api.news.Article
import com.example.mainichi.helper.api.news.Source

@Entity(tableName = "articles")
data class DbArticle(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val sourceID: String?,
    val sourceName : String,
    @PrimaryKey val title: String,
    val url: String?,
    val urlToImage: String?
)

fun DbArticle.toArticle() = Article(
    author = this.author,
    content = this.content,
    description = this.description,
    publishedAt = this.publishedAt,
    source = Source(id = this.sourceID, name = this.sourceName),
    title = this.title,
    url = this.url,
    urlToImage = this.urlToImage
)