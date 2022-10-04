package com.bknz.mainichi.core.model

data class UserData(
    val authenticatedAnonymously : Boolean = false,
    val authenticatedWithEmail : Boolean = false,
    val name : String? = null,
    val email : String? = null
)
