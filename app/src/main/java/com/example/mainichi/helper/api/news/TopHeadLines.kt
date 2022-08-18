package bknz.example.mainichi.helper.api.news

@kotlinx.serialization.Serializable
data class TopHeadlines(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)