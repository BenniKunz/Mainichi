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
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class SettingsViewModel @Inject constructor(
    val api: CryptoAPI,
    val database : AppDatabase,
    private val workManager: WorkManager

) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(
            UiState(
                isLoading = true,
                notificationParameters = notificationParameters
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
                assets = assets
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
                            event = event.eventType,
                            eventValue = event.eventValue,
                            repeatIntervalTimeUnit = event.repeatIntervalTimeUnit,
                            repeatInterval = event.repeatInterval
                        )
                    }
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun createCustomUserNotification(
        asset: String,
        event: String,
        eventValue: String,
        repeatIntervalTimeUnit: TimeUnit,
        repeatInterval: Long
    ) {

        val tag = ("${asset}_${event}_${eventValue}_${repeatIntervalTimeUnit}_${repeatInterval}").filter { !it.isWhitespace() }
        Log.d("Notification Test", tag)

        database.notificationsDao().insertNotification(DbNotification(
            tag = tag,
            name = asset,
            image = uiState.value.assets.find { it.name == asset }?.image ?: "",
            symbol = uiState.value.assets.find { it.name == asset }?.symbol ?: "",
            event = event,
            interval = repeatInterval.toString(),
            date = LocalDateTime.now().toString()
        ))

        val priceChangeWorker = initializeDelayedPeriodicWorker<PriceChangeWorker>(
            workerStartingDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
            workerStartingHour = LocalDateTime.now().hour.toLong(),
            workerStartingMinute = LocalDateTime.now().minute.toLong() + 1,
            tag = tag,
            repeatInterval = repeatInterval,
            repeatIntervalTimeUnit = repeatIntervalTimeUnit,
            asset = asset
        )

        workManager.enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.KEEP,
            priceChangeWorker
        )
    }


    companion object {
        private val notificationParameters = NotificationParameters(
            priceEvents = listOf("Price up", "Price down", "Price up or down"),
            percentageThresholds = listOf("5.0 %", "10.0 %", "15.0 %"),
            intervalTypes = listOf("Day", "Hour"),
            intervalValues = listOf("1", "2", "3", "4", "5", "6", "7")
        )
    }
}

data class NotificationParameters(
    val priceEvents: List<String> = emptyList(),
    val percentageThresholds: List<String> = emptyList(),
    val intervalTypes: List<String> = emptyList(),
    val intervalValues: List<String> = emptyList()
)

@RequiresApi(Build.VERSION_CODES.O)
private inline fun <reified T : CoroutineWorker> initializeDelayedPeriodicWorker(
    workerStartingHour: Long,
    workerStartingMinute: Long,
    workerStartingDay: Int,
    tag: String,
    repeatInterval: Long,
    repeatIntervalTimeUnit: TimeUnit,
    asset: String
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
                .putString("asset", asset)
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