package com.example.mainichi

import android.app.Application
import androidx.room.Room
import com.example.mainichi.helper.db.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainichiApplication : Application() {

//    //TODO Hilt
//    val database: AppDatabase by lazy {
//        Room.databaseBuilder(
//            applicationContext, AppDatabase::class.java,
//            "database.db"
//        )
//            .fallbackToDestructiveMigration()
//            .build()
//    }

//    companion object {
//
//        @JvmField
//        var appInstance: MainichiApplication? = null
//
//
//
//        @JvmStatic fun getAppInstance(): MainichiApplication {
//            return appInstance as MainichiApplication
//        }
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        appInstance=this
//    }


}