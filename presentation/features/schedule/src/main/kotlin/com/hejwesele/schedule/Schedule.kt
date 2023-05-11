package com.hejwesele.schedule

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.ErrorData
import com.hejwesele.android.components.ErrorView
import com.hejwesele.android.components.HorizontalMargin
import com.hejwesele.android.components.Loader
import com.hejwesele.android.components.TextPlaceholder
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.extensions.disabled
import com.hejwesele.internet.InternetConnectionPopup
import com.hejwesele.schedule.model.ActivityUiModel
import com.hejwesele.schedule.model.TimerUiModel
import com.hejwesele.theme.R
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun Schedule(navigation: IScheduleNavigation) = ScheduleEntryPoint(navigation = navigation)

@Composable
private fun ScheduleEntryPoint(
    viewModel: ScheduleViewModel = hiltViewModel(),
    navigation: IScheduleNavigation
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    }

    val uiState by viewModel.states.collectAsState()
    val uiEvents by viewModel.events.collectAsState()

    ScheduleEventHandler(
        events = uiEvents,
        viewModel = viewModel,
        navigation = navigation
    )

    val scheduleData = with(uiState) {
        ScheduleData(
            isLoading = isLoading,
            isEnabled = isEnabled,
            activities = activities,
            timer = timer,
            internetPopupEnabled = true,
            errorData = errorData
        )
    }

    ScheduleScreen(data = scheduleData)
}

@Composable
private fun ScheduleEventHandler(
    events: ScheduleUiEvents,
    viewModel: ScheduleViewModel,
    navigation: IScheduleNavigation
) {
    EventEffect(
        event = events.logout,
        onConsumed = viewModel::onLogoutPerformed,
        action = navigation::logout
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
private fun ScheduleScreen(data: ScheduleData) {
    Scaffold {
        Column(modifier = Modifier.fillMaxSize()) {
            if (data.internetPopupEnabled) {
                InternetConnectionPopup()
            }
            Box(
                modifier = Modifier
                    .weight(1.0f)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                when {
                    data.isLoading -> Loader()
                    !data.isEnabled -> TextPlaceholder(text = Label.scheduleDisabledMessageText)
                    data.errorData != null -> ErrorView(modifier = Modifier.fillMaxSize())
                    else -> ScheduleContent(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface),
                        activities = data.activities,
                        timerVisible = data.timer != null,
                        timerText = data.timer?.text,
                        timerProgress = data.timer?.progress
                    )
                }
            }
        }
    }
}

@Composable
private fun ScheduleContent(
    modifier: Modifier = Modifier,
    activities: List<ActivityUiModel>,
    timerVisible: Boolean,
    timerText: String?,
    timerProgress: Float?
) {
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = Dimension.marginLarge,
                end = Dimension.marginLarge,
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + Dimension.marginNormal,
                bottom = Dimension.marginNormal
            )
        ) {
            activities.forEach { activity ->
                item {
                    ActivityItem(
                        modifier = Modifier.fillMaxWidth(),
                        activity = activity
                    )
                }
                item { VerticalMargin(Dimension.marginNormal) }
            }
        }
        if (timerVisible) {
            Timer(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = Dimension.marginSmall,
                        bottom = Dimension.marginSmall
                    )
                    .size(72.dp),
                text = timerText.orEmpty(),
                progress = timerProgress ?: 0.0f
            )
        }
    }
}

@Composable
private fun ActivityItem(
    modifier: Modifier = Modifier,
    activity: ActivityUiModel
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_ellipse_thin),
            contentDescription = null,
            modifier = Modifier.size(Dimension.iconSmall),
            tint = MaterialTheme.colorScheme.primary
        )
        HorizontalMargin(Dimension.marginSmall)
        Text(
            text = activity.time,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        HorizontalMargin(Dimension.marginSmall)
        Column(modifier = Modifier.weight(1.0f)) {
            Text(
                text = activity.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            if (!activity.description.isNullOrEmpty()) {
                VerticalMargin(Dimension.marginExtraSmall)
                Text(
                    text = activity.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        HorizontalMargin(Dimension.marginSmall)
        Icon(
            painter = painterResource(id = activity.typeIconResId),
            contentDescription = null,
            modifier = Modifier.size(Dimension.iconSmall),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun Timer(
    modifier: Modifier = Modifier,
    text: String,
    progress: Float
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.tertiaryContainer.disabled,
        shape = CircleShape
    ) {
        Box(
            modifier = Modifier.padding(Dimension.marginSmall),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
        CircularProgressIndicator(
            progress = progress,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

private data class ScheduleData(
    val isLoading: Boolean,
    val isEnabled: Boolean,
    val activities: List<ActivityUiModel>,
    val timer: TimerUiModel?,
    val internetPopupEnabled: Boolean,
    val errorData: ErrorData?
) {
    companion object {
        val Preview = ScheduleData(
            isLoading = false,
            isEnabled = true,
            activities = listOf(
                ActivityUiModel(
                    time = "20:00",
                    title = "Zabawa weselna",
                    typeIconResId = R.drawable.ic_party
                ),
                ActivityUiModel(
                    time = "21:00",
                    title = "Posiłek II",
                    description = "Kotlet schabowy z ziemniakami",
                    typeIconResId = R.drawable.ic_meal
                ),
                ActivityUiModel(
                    time = "00:00",
                    title = "Oczepiny",
                    typeIconResId = R.drawable.ic_attraction
                )
            ),
            timer = TimerUiModel(
                text = "20:00",
                progress = 0.5f
            ),
            internetPopupEnabled = false,
            errorData = null
        )
    }
}

@Preview
@Composable
private fun ScheduleScreenPreview() {
    AppTheme(darkTheme = false) {
        ScheduleScreen(data = ScheduleData.Preview)
    }
}
