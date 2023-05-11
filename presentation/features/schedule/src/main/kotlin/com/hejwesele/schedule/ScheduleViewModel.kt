package com.hejwesele.schedule

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.components.AlertData
import com.hejwesele.android.components.ErrorData
import com.hejwesele.android.mvvm.StateEventsViewModel
import com.hejwesele.android.theme.Label
import com.hejwesele.events.model.EventSettings
import com.hejwesele.schedule.model.ActivityUiModel
import com.hejwesele.schedule.model.TimerUiModel
import com.hejwesele.schedule.usecase.GetEventSettings
import com.hejwesele.schedule.usecase.ObserveClock
import com.hejwesele.schedule.usecase.ObserveSchedule
import com.hejwesele.schedules.model.Schedule
import com.hejwesele.schedules.model.ScheduleActivityType
import com.hejwesele.schedules.model.ScheduleActivityType.ATTRACTION
import com.hejwesele.schedules.model.ScheduleActivityType.CHURCH
import com.hejwesele.schedules.model.ScheduleActivityType.MEAL
import com.hejwesele.schedules.model.ScheduleActivityType.PARTY
import com.hejwesele.theme.R
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class ScheduleViewModel @Inject constructor(
    private val getEventSettings: GetEventSettings,
    private val observeSchedule: ObserveSchedule,
    private val observeClock: ObserveClock
) : StateEventsViewModel<ScheduleUiState, ScheduleUiEvents>(ScheduleUiState.Default, ScheduleUiEvents.Default) {

    companion object {
        private const val CLOCK_TICK_TIME_SEC = 30
        private const val TIME_PATTERN = "HH:mm"
    }

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            getEventSettings()
                .onSuccess { settings -> handleEventSettings(settings) }
                .onFailure { handleEventSettingsError() }
        }
    }

    fun onLogoutPerformed() {
        viewModelScope.launch {
            updateEvents { copy(logout = consumed) }
        }
    }

    private suspend fun handleEventSettings(settings: EventSettings) {
        val scheduleId = settings.scheduleId

        if (scheduleId != null) {
            observeSchedule(scheduleId)
                .collect { handleScheduleResult(it) }
        } else {
            updateState {
                copy(isLoading = false, isEnabled = false)
            }
        }
    }

    private fun handleEventSettingsError() {
        updateState {
            copy(
                isLoading = false,
                alertData = AlertData.Default.copy(
                    title = Label.errorDescriptionEventNotFoundText,
                    onDismiss = ::onEventNotFoundAlertDismissed
                )
            )
        }
    }

    private fun handleScheduleResult(result: Result<Schedule>) {
        result
            .onSuccess { schedule ->
                setupTimer(schedule)

                val activities = schedule.activities.map {
                    ActivityUiModel(
                        time = it.startDate.formatTimeString(),
                        title = it.title,
                        description = it.description,
                        typeIconResId = getTypeIconResId(type = it.type)
                    )
                }

                updateState {
                    copy(
                        isLoading = false,
                        isEnabled = activities.isNotEmpty(),
                        activities = activities
                    )
                }
            }
            .onFailure {
                updateState { copy(isLoading = false, errorData = ErrorData.Default) }
            }
    }

    private fun setupTimer(schedule: Schedule) {
        timerJob?.cancel()

        if (schedule.activities.isNotEmpty()) {
            val startDate = schedule.activities.first().startDate
            val endDate = schedule.activities.last().endDate

            if (startDate >= endDate) {
                updateState { copy(timer = null) }
            } else {
                timerJob = viewModelScope.launch {
                    observeClock(CLOCK_TICK_TIME_SEC.seconds)
                        .collect { currentDate ->
                            val timer = determineTimerData(
                                currentDate = currentDate,
                                startDate = startDate,
                                endDate = endDate
                            )
                            updateState { copy(timer = timer) }
                        }
                }
            }
        } else {
            updateState { copy(timer = null) }
        }
    }

    private fun onEventNotFoundAlertDismissed() {
        updateState { copy(alertData = null) }
        updateEvents { copy(logout = triggered) }
    }

    private fun getTypeIconResId(type: ScheduleActivityType) = when (type) {
        CHURCH -> R.drawable.ic_church
        MEAL -> R.drawable.ic_meal
        ATTRACTION -> R.drawable.ic_attraction
        PARTY -> R.drawable.ic_party
    }

    private fun determineTimerData(
        currentDate: LocalDateTime,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): TimerUiModel? = when {
        currentDate < startDate -> TimerUiModel(
            text = startDate.formatTimeString(),
            progress = 0.0f
        )

        currentDate in startDate..endDate -> TimerUiModel(
            text = currentDate.formatTimeString(),
            progress = calculateEventProgress(
                currentDate = currentDate,
                startDate = startDate,
                endDate = endDate
            )
        )

        currentDate > endDate -> TimerUiModel(
            text = endDate.formatTimeString(),
            progress = 100.0f
        )

        else -> null
    }

    private fun calculateEventProgress(
        currentDate: LocalDateTime,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Float = ((currentDate - startDate) / (endDate - startDate)).toFloat()

    private fun LocalDateTime.formatTimeString() =
        DateTimeFormatter.ofPattern(TIME_PATTERN).format(toJavaLocalDateTime())

    private operator fun LocalDateTime.minus(other: LocalDateTime): Duration {
        val timeZone = TimeZone.currentSystemDefault()
        val firstInstant = toInstant(timeZone)
        val otherInstant = other.toInstant(timeZone)

        return firstInstant - otherInstant
    }
}

internal data class ScheduleUiState(
    val isLoading: Boolean,
    val isEnabled: Boolean,
    val activities: List<ActivityUiModel>,
    val timer: TimerUiModel?,
    val errorData: ErrorData?,
    val alertData: AlertData?
) {
    companion object {
        val Default = ScheduleUiState(
            isLoading = false,
            isEnabled = true,
            activities = emptyList(),
            timer = null,
            errorData = null,
            alertData = null
        )
    }
}

internal data class ScheduleUiEvents(
    val logout: StateEvent
) {
    companion object {
        val Default = ScheduleUiEvents(
            logout = consumed
        )
    }
}
