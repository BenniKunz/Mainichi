package com.bknz.mainichi.data

import android.content.Intent
import com.bknz.mainichi.core.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface UserRepository {

    val userData : MutableStateFlow<UserData>

    fun buildLogIntIntent() : Intent
    fun createAnonymousAccount()
    fun authenticate(email: String, password: String)
    fun createAccount(email: String, password: String)

}