package com.hejwesele.schedule

import androidx.lifecycle.viewModelScope
import com.hejwesele.android.components.AlertData
import com.hejwesele.android.components.ErrorData
import com.hejwesele.android.mvvm.StateEventsViewModel
import com.hejwesele.android.theme.Label
import com.hejwesele.events.model.EventSettings
import com.hejwesele.schedule.model.ActivityUiModel
import com.hejwesele.schedule.usecase.GetEventSettings
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ScheduleViewModel @Inject constructor(
    private val getEventSettings: GetEventSettings,
    private val observeSchedule: ObserveSchedule
) : StateEventsViewModel<ScheduleUiState, ScheduleUiEvents>(ScheduleUiState.Default, ScheduleUiEvents.Default) {

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
                val activities = schedule.activities.map {
                    ActivityUiModel(
                        time = it.startDate.time.toString(),
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
}

internal data class ScheduleUiState(
    val isLoading: Boolean,
    val isEnabled: Boolean,
    val activities: List<ActivityUiModel>,
    val errorData: ErrorData?,
    val alertData: AlertData?
) {
    companion object {
        val Default = ScheduleUiState(
            isLoading = false,
            isEnabled = true,
            activities = emptyList(),
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
