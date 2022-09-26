package com.bknz.mainichi.core.model

data class UserData(
    val authenticatedAnonymously : Boolean = false,
    val authenticatedCredentials : Boolean = false,
    val name : String? = null,
    val email : String? = null
)
