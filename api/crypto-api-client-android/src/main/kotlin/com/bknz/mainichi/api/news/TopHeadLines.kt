package com.bknz.mainichi.api.news

@kotlinx.serialization.Serializable
data class TopHeadlines(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)