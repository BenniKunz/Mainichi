package com.bknz.mainichi.data

import android.content.Intent
import android.util.Log
import com.bknz.mainichi.core.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
                        authenticatedWithEmail = true,
                        name = Firebase.auth.currentUser?.displayName
                            ?: Firebase.auth.currentUser?.email ?: "No Name"
                    )
                } else {
                    Log.d("Auth Test", "Email log in not successful: ${task.exception}")
                }
                onResult(task.exception)
            }
    }

    override fun authenticateWithMail(
        email: String,
        password: String,
        onResult: (Throwable?) -> Unit
    ) {
        Firebase.auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    userData.update {
                        UserData(
                            authenticatedAnonymously = false,
                            authenticatedWithEmail = true,
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

    override fun signOut() : Boolean
    {
        val x = Firebase.auth.currentUser

        if (x != null) {
            Firebase.auth.signOut()

            if (Firebase.auth.currentUser == null) {
                userData.update {
                    UserData(
                        authenticatedAnonymously = false,
                        authenticatedWithEmail = false
                    )
                }
                return true
            }
            return false
        }
        return false
    }
}