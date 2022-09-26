package com.bknz.mainichi.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bknz.mainichi.data.database.model.DbNotification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: DbNotification)

    @Query("DELETE FROM notification WHERE name = :name")
    suspend fun deleteFavoriteAsset(name: String)

    @Query("SELECT * FROM notification")
    fun getAllNotifications() : Flow<List<DbNotification>>
}