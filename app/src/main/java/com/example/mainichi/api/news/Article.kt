package com.example.mainichi.api.news

import com.example.mainichi.db.DbArticle

//@JsonClass(generateAdapter = true)
//data class Article(
//    @Json(name = "author")
//    val author: String?,
//    @Json(name = "content")
//    val content: String?,
//    @Json(name = "description")
//    val description: String?,
//    @Json(name = "publishedAt")
//    val publishedAt: String,
//    @Json(name = "source")
//    val source: Source,
//    @Json(name = "title")
//    val title: String,
//    @Json(name = "url")
//    val url: String?,
//    @Json(name = "urlToImage")
//    val urlToImage: String?
//)

@kotlinx.serialization.Serializable
data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String?,
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