package com.example.mainichi.api.news

//import com.example.mainichi.data.database.DbArticle

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

/*
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
)*/
