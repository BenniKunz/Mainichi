package com.bknz.mainichi.api.crypto

@kotlinx.serialization.Serializable
data class ROI(
    val currency: String,
    val percentage: Double,
    val times: Double
)