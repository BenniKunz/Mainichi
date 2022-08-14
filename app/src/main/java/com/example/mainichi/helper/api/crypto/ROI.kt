package com.example.mainichi.helper.api.crypto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ROI(
    @Json(name = "currency")
    val currency: String,
    @Json(name = "percentage")
    val percentage: Double,
    @Json(name = "times")
    val times: Double
)