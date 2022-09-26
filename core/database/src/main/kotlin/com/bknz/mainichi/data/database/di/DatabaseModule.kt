package com.bknz.mainichi.data.database.di

import android.content.Context
import androidx.room.Room
import com.bknz.mainichi.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDataBase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext, AppDatabase::class.java,
            "database.db"
        )
            .fallbackToDestructiveMigration()
            .build()
}
