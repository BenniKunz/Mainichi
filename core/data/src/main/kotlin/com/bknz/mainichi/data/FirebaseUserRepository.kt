package com.bknz.mainichi.data

import android.content.Intent
import android.util.Log
import androidx.annotation.MainThread
import com.bknz.mainichi.core.data.R
import com.bknz.mainichi.core.model.UserData
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FirebaseUserRepository @Inject constructor() : UserRepository {

    override val userData: MutableStateFlow<UserData> = MutableStateFlow(UserData())
    override fun buildLogIntIntent(): Intent {

////        val authUILayout = AuthMethodPickerLayout.Builder(0)
////            .setGoogleButtonId(0)
////            .setEmailButtonId(0)
////            .build()
//
//        return AuthUI.getInstance().createSignInIntentBuilder()
//            .setAvailableProviders(
//                listOf(
//                    AuthUI.IdpConfig.EmailBuilder().build(),
////                    AuthUI.IdpConfig.GoogleBuilder().build()
//                )
//            )
//            .enableAnonymousUsersAutoUpgrade()
//            .setLogo(0)
////            .setAuthMethodPickerLayout(authUILayout)
//            .build()
        return Intent()
    }

    override fun createAnonymousAccount(onResult: (Throwable?) -> Unit) {

        Firebase.auth.signInAnonymously().addOnCompleteListener { task ->

            if (task.isSuccessful) {

                userData.update {
                    UserData(
                        authenticatedAnonymously = true,
                        name = "Mr.Anonymous"
                    )
                }

            } else {
                Log.d("Auth Test", "Anonymous login not successful: ${task.exception}")
            }
            onResult(task.exception)
        }
    }

    override fun createEmailAccount(
        email: String,
        password: String,
        onResult: (Throwable?) -> Unit
    ) {

        Firebase.auth.createUserWithEmailAndPassword(
            /* email = */ email.trim(),
            /* password = */password
        )
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    UserData(
                        authenticatedAnonymously = false,
                        authenticatedCredentials = true,
                        name = Firebase.auth.currentUser?.displayName
                            ?: Firebase.auth.currentUser?.email ?: "No Name"
                    )
                } else {
                    Log.d("Auth Test", "Email log in not successful: ${task.exception}")
                }
                onResult(task.exception)
            }
    }

    override fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit) {

        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    userData.update {
                        UserData(
                            authenticatedAnonymously = false,
                            authenticatedCredentials = true,
                            name = Firebase.auth.currentUser?.displayName
                                ?: Firebase.auth.currentUser?.email ?: "No Name"
                        )
                    }
                } else {
                    Log.d("Auth Test", "Email log in not successful")
                }
                onResult(task.exception)
            }
    }
}