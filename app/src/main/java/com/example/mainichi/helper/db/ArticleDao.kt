package com.example.mainichi.helper.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("select * from articles")
    fun getAllArticles() : PagingSource<Int, DbArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<DbArticle>)

    @Query("SELECT * FROM articles ORDER BY publishedAt DESC LIMIT 1")
    suspend fun getLatestArticle(): DbArticle?

    @Query("SELECT * FROM articles")
    fun observeLatestArticles(): Flow<List<DbArticle>>

    @Query("DELETE FROM articles")
    suspend fun deleteArticles()

    @Query("select count(*) from articles")
    suspend fun count() : Long

//
//    @Query("select * from word where value like :term || '%' order by value")
//    fun searchAll(term : String?): PagingSource<Int, LocalWord>

}