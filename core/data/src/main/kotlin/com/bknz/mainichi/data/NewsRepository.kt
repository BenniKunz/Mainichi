package com.bknz.mainichi.data

import com.bknz.mainichi.api.news.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getAllArticles(): Flow<List<Article>>

    suspend fun refresh()
}