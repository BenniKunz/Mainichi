package com.bknz.mainichi.data

import android.util.Log
import com.bknz.mainichi.api.news.Article
import com.bknz.mainichi.api.news.NewsAPI
import com.bknz.mainichi.data.database.ArticleDao
import com.bknz.mainichi.data.database.model.toArticle
import com.bknz.mainichi.data.database.model.toDbArticle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ArticleRepository @Inject constructor(
    private val api: NewsAPI,
    private val articleDao: ArticleDao
) : NewsRepository {

    private val repositoryScope = CoroutineScope(context = Dispatchers.IO)

    override fun getAllArticles(): Flow<List<Article>> {

        //Check for updates asynchronously
        repositoryScope.launch {
            refresh()
        }

        //Immediately return the latest database snapshot
        return articleDao.observeLatestArticles()
            .map { articles -> articles.map { it.toArticle() } }
    }
//        return db.observeLatestArticles().distinctUntilChanged().filterNotNull().map { it.toArticle() } }

    override suspend fun refresh() {
//        val persistedArticle = db.getLatestArticle()
//        val today = DateTime.now().withTimeAtStartOfDay()

//            Log.d("Sample", "Hello DB! size: ${db.count()}")

        var headlines = api.getTopHeadlines()
        Log.d("API called", "News API Called!!!")

//        Persist the newly fetched articles in the database

        articleDao.insertArticles(headlines.articles.map { it.toDbArticle() })

//        If no article is existent in the database or the newest article is not from today fetch a new one
//        if (persistedArticle == null)  || persistedArticle.publishedAt.withTimeAtStartOfDay().isBefore(today)) {
//            Log.d("Sample", "Latest articles are stale, fetching newest articles from API")
//
//            val headlines = api.getTopHeadlines()
//
//            //Persist the newly fetched articles in the database
//            headlines.articles.forEach { article ->
//                db.insertArticle(article.toDbArticle())
//            }
        //because the repository already observes the database as flow the newest value will be automatically propagated
//        }
    }
}
