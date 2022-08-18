package com.example.mainichi.helper.api.news

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//@JsonClass(generateAdapter = true)
//data class Source(
//    @Json(name = "id")
//    val id: String?,
//    @Json(name = "name")
//    val name: String
//)

@kotlinx.serialization.Serializable
data class Source(
    val id: String?,
    val name: String
)