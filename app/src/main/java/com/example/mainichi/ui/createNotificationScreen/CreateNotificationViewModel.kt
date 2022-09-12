package com.example.mainichi.ui.createNotificationScreen


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.mainichi.api.crypto.CryptoAPI
import com.example.mainichi.api.crypto.asAsset
import com.example.mainichi.data.database.AppDatabase
import com.example.mainichi.data.database.DbNotification
import com.example.mainichi.ui.createNotificationScreen.CreateNotificationContract.*
import com.example.mainichi.ui.createNotificationScreen.CreateNotificationContract.NotificationConfiguration.*
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
class CreateNotificationViewModel @Inject constructor(
    val api: CryptoAPI,
    val database: AppDatabase,
    private val workManager: WorkManager

) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(
            UiState(
                isLoading = true,
                NotificationConfiguration()
            )
        )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<CreateNotificationEvent> = MutableSharedFlow()

    fun setEvent(event: CreateNotificationEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    private val _effect: Channel<CreateNotificationEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setEffect(builder: () -> CreateNotificationEffect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }


    init {

        viewModelScope.launch {

            loadData()

            handleEvents()
        }
    }

//    private suspend fun observeNotifications() {
//        withContext(Dispatchers.IO) {
//            database.notificationsDao().getAllNotifications().collect { dbNotificationList ->
//
//                _uiState.update { uiState ->
//
//                    uiState.copy(notifications = dbNotificationList.map { dbNotification ->
//                        AssetNotification(
//                            name = dbNotification.name,
//                            image = dbNotification.image,
//                            symbol = dbNotification.symbol,
//                            event = dbNotification.event,
//                            eventValue = dbNotification.eventValue,
//                            intervalType = dbNotification.intervalType,
//                            interval = dbNotification.interval,
//                            date = dbNotification.date
//                        )
//                    })
//                }
//            }
//        }
//    }

    private suspend fun loadData() {
        val assets = withContext(Dispatchers.IO) {
            api.getAllCryptoAssets().map { apiAsset ->
                apiAsset.asAsset()
            }
        }

        _uiState.update { uiState ->

            uiState.copy(
                isLoading = false,
                notificationConfiguration = NotificationConfiguration(
                    assets = assets

                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun handleEvents() {
        _event
            .debounce(timeoutMillis = 300)
            .collect { event ->

                when (event) {

                    is CreateNotificationEvent.CreateCustomNotification -> {

                        createCustomUserNotification(
                            notification = uiState.value.notificationConfiguration
                        )

                        _uiState.update { uiState ->

                            uiState.copy(
                                notificationConfiguration = uiState.notificationConfiguration.copy(
                                    eventType = PriceEvent.None,
                                    eventValue = "",
                                    anyEventValue = false,
                                    intervalValue = "",
                                    intervalType = Periodically.Hourly
                                )
                            )
                        }
                    }

                    is CreateNotificationEvent.SelectAsset -> {

                        _uiState.update { uiState ->

                            uiState.copy(
                                notificationConfiguration = uiState.notificationConfiguration.copy(
                                    assets = uiState.notificationConfiguration.assets.map { selectableAsset ->

                                        if (selectableAsset == event.selectedAsset) {
                                            selectableAsset.copy(isSelected = true)
                                        } else {
                                            if (selectableAsset.isSelected) {
                                                selectableAsset.copy(isSelected = false)
                                            } else {
                                                selectableAsset
                                            }
                                        }
                                    }.sortedBy { !it.isSelected })

                            )
                        }
                    }
                    is CreateNotificationEvent.SelectIntervalPeriod -> {
                        _uiState.update { uiState ->

                            uiState.copy(
                                notificationConfiguration = uiState.notificationConfiguration.copy(
                                    intervalType = event.period
                                )
                            )
                        }
                    }
                    is CreateNotificationEvent.ChangeIntervalValue -> {

                        _uiState.update { uiState ->

                            uiState.copy(
                                notificationConfiguration = uiState.notificationConfiguration.copy(
                                    intervalValue = event.intervalValue
                                )
                            )
                        }
                    }
                    is CreateNotificationEvent.ChangeEventValue -> {
                        _uiState.update { uiState ->

                            uiState.copy(
                                notificationConfiguration = uiState.notificationConfiguration.copy(
                                    eventValue = event.eventValue
                                )
                            )
                        }
                    }
                    is CreateNotificationEvent.ChangePriceEvent -> {

                        _uiState.update { uiState ->

                            uiState.copy(
                                notificationConfiguration = uiState.notificationConfiguration.copy(
                                    eventType = event.priceEvent
                                )
                            )
                        }
                    }
                    is CreateNotificationEvent.ToggleAnyValue -> {
                        _uiState.update { uiState ->

                            uiState.copy(
                                notificationConfiguration = uiState.notificationConfiguration.copy(
                                    anyEventValue = !uiState.notificationConfiguration.anyEventValue,
                                    eventValue = ""
                                )
                            )
                        }
                    }
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun createCustomUserNotification(
        notification: NotificationConfiguration
    ) {

        val tag =
            ("${notification.assets[0].name}_${notification.eventType}_${notification.eventValue.ifEmpty { "0" }}_${notification.intervalType}_${notification.intervalValue}")
                .filter { !it.isWhitespace() }

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

        database.notificationsDao().insertNotification(
            DbNotification(
                tag = tag,
                name = notification.assets[0].name,
                image = uiState.value.notificationConfiguration.assets[0].image,
                symbol = uiState.value.notificationConfiguration.assets[0].symbol,
                event = notification.eventType.toString(),
                eventValue = notification.eventValue.ifEmpty {
                    "0"
                },
                interval = notification.intervalValue,
                intervalType = notification.intervalType.toString(),
                date = LocalDateTime.now().format(formatter)
            )
        )

        val priceChangeWorker = initializeDelayedPeriodicWorker<PriceChangeWorker>(
            workerStartingDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
            workerStartingHour = LocalDateTime.now().hour.toLong(),
            workerStartingMinute = LocalDateTime.now().minute.toLong() + 1,
            tag = tag,
            notification = notification
        )

        workManager.enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.KEEP,
            priceChangeWorker
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private inline fun <reified T : CoroutineWorker> initializeDelayedPeriodicWorker(
    workerStartingHour: Long,
    workerStartingMinute: Long,
    workerStartingDay: Int,
    tag: String,
    notification: NotificationConfiguration
): PeriodicWorkRequest {
    val workerDelay: Duration = calculateDurationUntilStart(
        workerStartingHour = workerStartingHour,
        workerStartingMinute = workerStartingMinute,
        workerStartingDay = workerStartingDay
    )

    Log.d("Notification Test", workerDelay.toMillis().toString())

    return PeriodicWorkRequestBuilder<T>(
        repeatInterval = notification.intervalValue.toLong(),
        repeatIntervalTimeUnit = when (notification.intervalType) {
            Periodically.Daily -> TimeUnit.DAYS
            Periodically.Hourly -> TimeUnit.HOURS
        }
    )
        .setInitialDelay(workerDelay.toMillis(), TimeUnit.MILLISECONDS)
        .setInputData(
            Data.Builder()
                .putString("asset", notification.assets[0].name)
                .putString("event", notification.eventType.toString())
                .putString("eventValue", notification.eventValue.ifEmpty { "0" })
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