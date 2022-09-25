package com.example.mainichi.core.model

data class NotificationConfiguration(
    val assets: List<Asset> = emptyList(),
//        val notifyIncrease: Boolean = false,
//        val notifyDecrease: Boolean = false,

    val eventType: PriceEvent = PriceEvent.None,
    val eventValue: String = "",
    val anyEventValue: Boolean = false,

    val intervalValue: String = "",
    val intervalType: Periodically = Periodically.Hourly,

    val date: String = ""
) {

    enum class Periodically {
        Hourly, Daily
    }

    enum class PriceEvent {
        None,
        PriceUp,
        PriceDown,
        PriceUpDown
    }
}