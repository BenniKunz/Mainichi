package com.bknz.mainichi.data

import android.content.Intent
import com.bknz.mainichi.core.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow

interface UserRepository {

    val userData : MutableStateFlow<UserData>

    fun buildLogIntIntent() : Intent
    fun createAnonymousAccount(onResult: (Throwable?) -> Unit)
    fun createEmailAccount(email: String, password: String, onResult: (Throwable?) -> Unit)
    fun authenticateWithMail(email: String, password: String, onResult: (Throwable?) -> Unit)
    fun signOut() : Boolean
}