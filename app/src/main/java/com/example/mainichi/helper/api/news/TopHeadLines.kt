package com.example.mainichi.helper.api.news

import com.example.mainichi.helper.api.news.Article

@kotlinx.serialization.Serializable
data class TopHeadlines(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)