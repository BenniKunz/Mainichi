package com.example.mainichi.ui.settingsScreen


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.mainichi.api.crypto.CryptoAPI
import com.example.mainichi.api.crypto.asAsset
import com.example.mainichi.db.AppDatabase
import com.example.mainichi.db.DbNotification
import com.example.mainichi.ui.settingsScreen.SettingsContract.*
import com.example.mainichi.worker.PriceChangeWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class SettingsViewModel @Inject constructor(
    val api: CryptoAPI,
    val database: AppDatabase,
    private val workManager: WorkManager

) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(
            UiState(
                isLoading = true,
                categoryMap = categoryMap
            )
        )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<SettingsEvent> = MutableSharedFlow()

    fun setEvent(event: SettingsEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    private val _effect: Channel<SettingsEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setEffect(builder: () -> SettingsEffect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }


    init {

        viewModelScope.launch {

            loadData()

            handleEvents()
        }

        viewModelScope.launch {

            observeNotifications()
        }
    }

    private suspend fun observeNotifications() {
        withContext(Dispatchers.IO) {
            database.notificationsDao().getAllNotifications().collect { dbNotificationList ->

                _uiState.update { uiState ->

                    uiState.copy(notifications = dbNotificationList.map { dbNotification ->
                        AssetNotification(
                            name = dbNotification.name,
                            image = dbNotification.image,
                            symbol = dbNotification.symbol,
                            event = dbNotification.event,
                            eventValue = dbNotification.eventValue,
                            intervalType = dbNotification.intervalType,
                            interval = dbNotification.interval,
                            date = dbNotification.date
                        )
                    })
                }
            }
        }
    }

    private suspend fun loadData() {
        val assets = withContext(Dispatchers.IO) {
            api.getAllCryptoAssets().map { apiAsset ->
                apiAsset.asAsset()
            }
        }

        _uiState.update { uiState ->

            uiState.copy(
                isLoading = false,
                assets = assets,
                categoryMap = uiState.categoryMap
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun handleEvents() {
        _event
            .debounce(timeoutMillis = 300)
            .collect { event ->

                when (event) {

                    is SettingsEvent.CreateCustomNotification -> {

                        createCustomUserNotification(
                            asset = event.asset,
                            event = _uiState.value.categoryMap.getSelectedCategoryItem("Price Events"),
                            eventValue = _uiState.value.categoryMap.getSelectedCategoryItem("Event Value"),
                            repeatIntervalTimeUnit = _uiState.value.categoryMap.getSelectedCategoryItem(
                                "Interval Types"
                            ),
                            repeatInterval = _uiState.value.categoryMap.getSelectedCategoryItem("Interval Values")
                                .toLong()
                        )
                    }
                    is SettingsEvent.SelectCategoryItem -> {

                        _uiState.update { uiState ->

                            uiState.copy(
                                categoryMap = uiState.categoryMap.mapValues { categoryList ->

                                    if (categoryList.value.find { it.categoryType == event.categoryType } != null) {
                                        return@mapValues categoryList.value.toMutableList()
                                            .map { categoryItem ->
                                                if (categoryItem.categoryType == event.categoryType) {
                                                    categoryItem.copy(selected = true)
                                                } else {
                                                    if (categoryItem.selected) {
                                                        categoryItem.copy(selected = false)
                                                    } else {
                                                        categoryItem
                                                    }
                                                }
                                            }
                                    }
                                    categoryList.value
                                })
                        }
                    }
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun createCustomUserNotification(
        asset: String,
        event: String,
        eventValue: String,
        repeatIntervalTimeUnit: String,
        repeatInterval: Long
    ) {

        val tag =
            ("${asset}_${event}_${eventValue}_${repeatIntervalTimeUnit}_${repeatInterval}").filter { !it.isWhitespace() }

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

        database.notificationsDao().insertNotification(
            DbNotification(
                tag = tag,
                name = asset,
                image = uiState.value.assets.find { it.name == asset }?.image ?: "",
                symbol = uiState.value.assets.find { it.name == asset }?.symbol ?: "",
                event = event,
                eventValue = if (eventValue == "Any change") {
                    "0"
                } else {
                    eventValue
                },
                interval = repeatInterval.toString(),
                intervalType = repeatIntervalTimeUnit,
                date = LocalDateTime.now().format(formatter)
            )
        )

        val priceChangeWorker = initializeDelayedPeriodicWorker<PriceChangeWorker>(
            workerStartingDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
            workerStartingHour = LocalDateTime.now().hour.toLong(),
            workerStartingMinute = LocalDateTime.now().minute.toLong() + 1,
            tag = tag,
            repeatInterval = repeatInterval,
            repeatIntervalTimeUnit = when (repeatIntervalTimeUnit) {
                "Day" -> TimeUnit.DAYS
                "Hour" -> TimeUnit.HOURS

                else -> throw IllegalStateException("This time Unit is not defined")
            },
            notification = AssetNotification(
                name = asset,
                image = uiState.value.assets.find { it.name == asset }?.image ?: "",
                symbol = uiState.value.assets.find { it.name == asset }?.symbol ?: "",
                event = event,
                eventValue = if (eventValue == "Any change") {
                    "0"
                } else {
                    eventValue
                },
                intervalType = repeatIntervalTimeUnit,
                interval = repeatInterval.toString(),
                date = LocalDateTime.now().toString()
            )
        )

        workManager.enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.KEEP,
            priceChangeWorker
        )
    }

    companion object {

        private val priceEvents = listOf(
            CategoryItem(
                categoryType = CategoryValues.PRICEUP
            ),
            CategoryItem(
                categoryType = CategoryValues.PRICEDOWN
            ),
            CategoryItem(
                categoryType = CategoryValues.PRICEUPORDOWN
            )
        )

        private val eventValues = listOf(
            CategoryItem(
                categoryType = CategoryValues.ANYCHANGE
            ),
            CategoryItem(
                categoryType = CategoryValues.FIVEPERCENT
            ),
            CategoryItem(
                categoryType = CategoryValues.TENPERCENT
            )
        )

        private val intervalTypes = listOf(
            CategoryItem(
                categoryType = CategoryValues.DAY
            ),
            CategoryItem(
                categoryType = CategoryValues.HOUR
            )
        )

        private val intervalValues = listOf(
            CategoryItem(
                categoryType = CategoryValues.ONE
            ),
            CategoryItem(
                categoryType = CategoryValues.TWO
            ),
            CategoryItem(
                categoryType = CategoryValues.THREE
            )
        )

        val categoryMap = mapOf(
            "Price Events" to priceEvents,
            "Event Value" to eventValues,
            "Interval Types" to intervalTypes,
            "Interval Values" to intervalValues
        )
    }
}

fun Map<String, List<CategoryItem>>.getSelectedCategoryItem(key: String): String {

    this[key]?.forEach { categoryItem ->
        if (categoryItem.selected) {
            return categoryItem.categoryType.categoryName
        }
    }
    return ""
}

data class CategoryItem(
    val categoryType: CategoryValues,
    val selected: Boolean = false
)

enum class CategoryValues(var categoryName: String) {

    ONE("1"),
    TWO("2"),
    THREE("3"),

    DAY("Day"),
    HOUR("Hour"),

    ANYCHANGE("Any change"),
    FIVEPERCENT("5"),
    TENPERCENT("10"),

    PRICEUP("Price up"),
    PRICEDOWN("Price down"),
    PRICEUPORDOWN("Price up or down")
}


@RequiresApi(Build.VERSION_CODES.O)
private inline fun <reified T : CoroutineWorker> initializeDelayedPeriodicWorker(
    workerStartingHour: Long,
    workerStartingMinute: Long,
    workerStartingDay: Int,
    tag: String,
    repeatInterval: Long,
    repeatIntervalTimeUnit: TimeUnit,
    notification: AssetNotification?
): PeriodicWorkRequest {
    val workerDelay: Duration = calculateDurationUntilStart(
        workerStartingHour = workerStartingHour,
        workerStartingMinute = workerStartingMinute,
        workerStartingDay = workerStartingDay
    )

    Log.d("Notification Test", workerDelay.toMillis().toString())

    return PeriodicWorkRequestBuilder<T>(
        repeatInterval = repeatInterval,
        repeatIntervalTimeUnit = repeatIntervalTimeUnit
    )
        .setInitialDelay(workerDelay.toMillis(), TimeUnit.MILLISECONDS)
        .setInputData(
            Data.Builder()
                .putString("asset", notification?.name)
                .putString("event", notification?.event)
                .putString("eventValue", notification?.eventValue)
                .build()
        )
        .setBackoffCriteria(
            BackoffPolicy.LINEAR,
            PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS
        )
        .addTag(tag)
        .build()
}

@RequiresApi(Build.VERSION_CODES.O)
private fun calculateDurationUntilStart(
    workerStartingHour: Long,
    workerStartingMinute: Long,
    workerStartingDay: Int = Calendar.TUESDAY
): Duration {
    val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

    val currentDateTime = LocalDateTime.now()

    var dueDay = workerStartingDay

    val dayDifference = dueDay - currentDay

    var delayInDays = 0L

    when {
        dayDifference < 0 -> {
            delayInDays += Calendar.SATURDAY + dayDifference
        }
        dayDifference == 0 &&
                workerStartingHour < currentDateTime.hour -> {
            delayInDays += Calendar.SATURDAY
        }
        dayDifference == 0 &&
                workerStartingHour.toInt() == currentDateTime.hour &&
                workerStartingMinute <= currentDateTime.minute -> {
            delayInDays += Calendar.SATURDAY
        }
        else -> {
            delayInDays += dayDifference
        }
    }

    val firstExecutionDate: LocalDateTime = currentDateTime
        .plusDays(delayInDays)
        .plusHours(workerStartingHour - currentDateTime.hour)
        .plusMinutes(workerStartingMinute - currentDateTime.minute)
        .plusSeconds(0L - currentDateTime.second)

    return Duration.between(currentDateTime, firstExecutionDate)
}