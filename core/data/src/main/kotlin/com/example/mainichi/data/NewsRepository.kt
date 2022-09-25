package com.example.mainichi.data

import com.example.mainichi.api.news.Article
import com.example.mainichi.data.database.model.toArticle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

interface NewsRepository {

    fun getAllArticles(): Flow<List<Article>>

    suspend fun refresh()
}