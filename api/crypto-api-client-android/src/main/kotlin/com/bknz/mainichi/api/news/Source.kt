package com.bknz.mainichi.api.news

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