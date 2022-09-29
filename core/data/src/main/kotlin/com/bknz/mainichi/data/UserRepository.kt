package com.bknz.mainichi.data

import android.content.Intent
import com.bknz.mainichi.core.model.UserData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface UserRepository {

    val userData : MutableStateFlow<UserData>

    fun buildLogIntIntent() : Intent
    fun createAnonymousAccount(onResult: (Throwable?) -> Unit)
    fun createEmailAccount(email: String, password: String, onResult: (Throwable?) -> Unit)
    fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit)

}