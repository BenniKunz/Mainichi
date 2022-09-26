package com.bknz.mainichi.data

import android.content.Intent
import android.util.Log
import com.bknz.mainichi.core.data.R
import com.bknz.mainichi.core.model.UserData
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseUserRepository @Inject constructor() : UserRepository {

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

    override fun createAnonymousAccount() {

        Firebase.auth.signInAnonymously().addOnCompleteListener { task ->

            if (task.isSuccessful) {

                Log.d("Anonymous Auth", "successful")
                userData.update {
                    UserData(authenticatedAnonymously = true)
                }
            } else {
                Log.d("Anonymous Auth", "not successful")
            }
        }
    }

    override fun authenticate(email: String, password: String) {

        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                }

            }
    }

    override fun createAccount(email: String, password: String) {

        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                }

            }
    }


}