package com.example.mainichi.helper.api.news

import com.example.mainichi.helper.db.DbArticle
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Article(
    @Json(name = "author")
    val author: String?,
    @Json(name = "content")
    val content: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "publishedAt")
    val publishedAt: String,
    @Json(name = "source")
    val source: Source,
    @Json(name = "title")
    val title: String,
    @Json(name = "url")
    val url: String?,
    @Json(name = "urlToImage")
    val urlToImage: String?
)

fun Article.toDbArticle() = DbArticle(
    author = this.author,
    content = this.content,
    description = this.description,
    publishedAt = this.publishedAt,
    sourceID = this.source.id,
    sourceName = this.source.name,
    title = this.title,
    url = this.url,
    urlToImage = this.urlToImage
)