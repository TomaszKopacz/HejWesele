package com.hejwesele.schedule

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.components.AlertData
import com.hejwesele.android.components.ErrorData
import com.hejwesele.android.mvvm.StateEventsViewModel
import com.hejwesele.android.theme.Label
import com.hejwesele.events.model.EventSettings
import com.hejwesele.schedule.model.ActivityProgress
import com.hejwesele.schedule.model.ActivityProgress.BEFORE
import com.hejwesele.schedule.model.ActivityProgress.IN_PROGRESS
import com.hejwesele.schedule.model.ActivityProgress.PAST
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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.combine
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
            val scheduleFlow = viewModelScope.async { observeSchedule(scheduleId) }
            val clockFlow = viewModelScope.async { observeClock(CLOCK_TICK_TIME_SEC.seconds) }

            scheduleFlow.await().combine(clockFlow.await()) { scheduleResult, time ->
                scheduleResult to time
            }.collect { (scheduleResult, time) ->
                handleScheduleResult(scheduleResult, time)
            }
        } else {
            emitDisabledState()
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

    private fun handleScheduleResult(result: Result<Schedule>, time: LocalDateTime) {
        result
            .onSuccess { schedule -> handleScheduleSuccessResult(schedule, time) }
            .onFailure { emitErrorState() }
    }

    private fun onEventNotFoundAlertDismissed() {
        updateState { copy(alertData = null) }
        updateEvents { copy(logout = triggered) }
    }

    private fun handleScheduleSuccessResult(schedule: Schedule, time: LocalDateTime) {
        if (schedule.activities.isNotEmpty()) {
            val startDate = schedule.activities.first().startDate
            val endDate = schedule.activities.last().endDate
            val activities = schedule.activities.map {
                ActivityUiModel(
                    time = it.startDate.formatTimeString(),
                    title = it.title,
                    description = it.description,
                    typeIconResId = getActivityTypeIcon(type = it.type),
                    progress = getActivityProgress(
                        currentDate = time,
                        startDate = it.startDate,
                        endDate = it.endDate
                    )
                )
            }
            val timer = determineTimerData(
                currentDate = time,
                startDate = startDate,
                endDate = endDate
            )
            emitSuccessState(
                activities = activities,
                timer = timer
            )
        } else {
            emitDisabledState()
        }
    }

    private fun emitSuccessState(activities: List<ActivityUiModel>, timer: TimerUiModel?) {
        updateState {
            copy(
                isLoading = false,
                isEnabled = true,
                activities = activities,
                timer = timer,
                errorData = null,
                alertData = null
            )
        }
    }

    private fun emitDisabledState() {
        updateState {
            copy(
                isLoading = false,
                isEnabled = false,
                activities = emptyList(),
                timer = null,
                errorData = null,
                alertData = null
            )
        }
    }

    private fun emitErrorState() {
        updateState {
            copy(
                isLoading = false,
                isEnabled = false,
                activities = emptyList(),
                timer = null,
                errorData = ErrorData.Default
            )
        }
    }

    private fun getActivityTypeIcon(type: ScheduleActivityType) = when (type) {
        CHURCH -> R.drawable.ic_church
        MEAL -> R.drawable.ic_meal
        ATTRACTION -> R.drawable.ic_attraction
        PARTY -> R.drawable.ic_party
    }

    private fun getActivityProgress(
        currentDate: LocalDateTime,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): ActivityProgress = when {
        currentDate < startDate -> BEFORE
        currentDate in startDate..endDate -> IN_PROGRESS
        currentDate > endDate -> PAST
        else -> BEFORE
    }

    private fun determineTimerData(
        currentDate: LocalDateTime,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): TimerUiModel? = when {
        startDate >= endDate -> null

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

    private fun LocalDateTime.formatTimeString(): String =
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
