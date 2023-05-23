package com.hejwesele.services.details

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.AlertData
import com.hejwesele.android.components.AlertDialog
import com.hejwesele.android.components.HorizontalMargin
import com.hejwesele.android.components.Loader
import com.hejwesele.android.components.RectangleImage
import com.hejwesele.android.components.ScreenOrientationLocker
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Lato
import com.hejwesele.extensions.openActivity
import com.hejwesele.internet.InternetConnectionPopup
import com.hejwesele.services.IServicesNavigation
import com.hejwesele.services.details.ServiceDetailToolbarData.PROGRESS_HALF_THRESHOLD
import com.hejwesele.services.model.IntentUiModel
import com.hejwesele.services.model.ServiceDetailsUiModel
import com.hejwesele.theme.R
import com.ramcosta.composedestinations.annotation.Destination
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScaffoldState
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
@Destination(navArgsDelegate = ServiceDetailsNavArgs::class)
fun ServiceDetails(navigation: IServicesNavigation) = ServiceDetailsEntryPoint(
    navigation = navigation
)

@Composable
private fun ServiceDetailsEntryPoint(
    viewModel: ServiceDetailsViewModel = hiltViewModel(),
    navigation: IServicesNavigation
) {
    ScreenOrientationLocker(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    }

    val uiState by viewModel.states.collectAsState()
    val uiEvents by viewModel.events.collectAsState()

    ServiceDetailsEventHandler(
        events = uiEvents,
        viewModel = viewModel,
        navigation = navigation
    )

    val serviceDetailsData = with(uiState) {
        ServiceDetailsData(
            isLoading = isLoading,
            service = service,
            internetPopupEnabled = true,
            alertData = alertData
        )
    }

    val serviceDetailsActions = with(viewModel) {
        ServiceDetailsActions(
            onIntentClick = ::onIntentSelected,
            onBackClick = ::onGoBack
        )
    }

    ServiceDetailsScreen(
        data = serviceDetailsData,
        actions = serviceDetailsActions
    )
}

@Composable
private fun ServiceDetailsEventHandler(
    events: ServicesDetailsUiEvents,
    viewModel: ServiceDetailsViewModel,
    navigation: IServicesNavigation
) {
    val context = LocalContext.current

    EventEffect(
        event = events.openIntent,
        onConsumed = viewModel::onIntentOpened,
        action = { intent -> openActivity(context, intent.intentPackage, intent.url) }
    )
    EventEffect(
        event = events.goBack,
        onConsumed = viewModel::onNavigatedBack,
        action = navigation::navigateUp
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
private fun ServiceDetailsScreen(
    data: ServiceDetailsData,
    actions: ServiceDetailsActions
) {
    Scaffold {
        Column(modifier = Modifier.fillMaxSize()) {
            if (data.internetPopupEnabled) {
                InternetConnectionPopup()
            }
            Box(
                modifier = Modifier
                    .weight(Dimension.weightFull)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                when {
                    data.isLoading -> Loader()
                    data.alertData != null -> AlertDialog(data = data.alertData)
                    data.service != null -> ServiceDetailsContent(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface),
                        service = data.service,
                        onIntentClick = { intent -> actions.onIntentClick(intent) },
                        onBackClick = actions.onBackClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ServiceDetailsContent(
    modifier: Modifier = Modifier,
    service: ServiceDetailsUiModel,
    onIntentClick: (IntentUiModel) -> Unit,
    onBackClick: () -> Unit
) {
    val state = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = modifier,
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            ServiceDetailsToolbar(
                state = state,
                title = service.name,
                imageUrl = service.imageUrl,
                onBackClick = onBackClick
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimension.marginLarge),
            contentPadding = WindowInsets.navigationBars.asPaddingValues()
        ) {
            service.details.forEach { (title, contents) ->
                item { VerticalMargin(Dimension.marginLarge) }
                item {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                item { VerticalMargin(Dimension.marginLarge) }

                contents.forEach { paragraph ->
                    item {
                        Text(
                            text = paragraph,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    item { VerticalMargin(Dimension.marginNormal) }
                }
            }
            item { VerticalMargin(Dimension.marginNormal) }
            item {
                IntentActions(
                    modifier = Modifier.fillMaxWidth(),
                    intents = service.intents,
                    onIntentClick = onIntentClick
                )
            }
            item { VerticalMargin(Dimension.marginNormal) }
        }
    }
}

private object ServiceDetailToolbarData {
    const val PROGRESS_HALF_THRESHOLD = 0.5f
}

@Composable
private fun CollapsingToolbarScope.ServiceDetailsToolbar(
    state: CollapsingToolbarScaffoldState,
    title: String,
    imageUrl: String,
    onBackClick: () -> Unit
) {
    val progress = state.toolbarState.progress
    val fontSizeMin = MaterialTheme.typography.headlineSmall.fontSize.value
    val fontSizeMax = MaterialTheme.typography.headlineLarge.fontSize.value
    val fontSize = (fontSizeMin + (fontSizeMax - fontSizeMin) * progress).sp
    val color = with(MaterialTheme.colorScheme) {
        if (progress < PROGRESS_HALF_THRESHOLD) onSurface else inverseOnSurface
    }

    RectangleImage(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(Dimension.aspectRatio_4_3)
            .parallax(ratio = Dimension.ratioParallax)
            .alpha(alpha = progress),
        url = imageUrl,
        contentScale = ContentScale.Crop
    )
    Text(
        text = title,
        modifier = Modifier
            .road(Alignment.CenterStart, Alignment.BottomEnd)
            .statusBarsPadding()
            .padding(
                start = Dimension.iconNormal + Dimension.marginSmall + Dimension.marginSmall,
                top = Dimension.marginSmall,
                end = Dimension.marginSmall,
                bottom = Dimension.marginSmall
            ),
        color = color,
        fontSize = fontSize,
        fontFamily = Lato,
        fontWeight = FontWeight.Normal
    )
    Icon(
        modifier = Modifier
            .statusBarsPadding()
            .padding(Dimension.marginSmall)
            .size(Dimension.iconNormal)
            .clip(MaterialTheme.shapes.small)
            .clickable { onBackClick() },
        contentDescription = null,
        painter = painterResource(R.drawable.ic_arrow_left),
        tint = color
    )
}

@Composable
private fun IntentActions(
    modifier: Modifier = Modifier,
    intents: List<IntentUiModel>,
    onIntentClick: (IntentUiModel) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        intents.forEach { intent ->
            HorizontalMargin(Dimension.marginSmall)
            Icon(
                modifier = Modifier
                    .size(Dimension.iconNormal)
                    .clip(MaterialTheme.shapes.small)
                    .clickable { onIntentClick(intent) },
                painter = painterResource(id = intent.iconResId),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private data class ServiceDetailsData(
    val isLoading: Boolean,
    val service: ServiceDetailsUiModel?,
    val internetPopupEnabled: Boolean,
    val alertData: AlertData?
) {
    companion object {
        val Preview = ServiceDetailsData(
            isLoading = false,
            service = ServiceDetailsUiModel(
                name = "Dj BoBo",
                imageUrl = "http://hejwesele.com/url1.jpg",
                details = listOf(
                    "DJ" to listOf("Best DJ ever")
                ),
                intents = listOf(
                    IntentUiModel(
                        iconResId = R.drawable.ic_instagram_primary,
                        intentPackage = null,
                        url = "fake url"
                    ),
                    IntentUiModel(
                        iconResId = R.drawable.ic_maps_primary,
                        intentPackage = null,
                        url = "fake url"
                    )
                )
            ),
            internetPopupEnabled = false,
            alertData = null
        )
    }
}

private data class ServiceDetailsActions(
    val onIntentClick: (IntentUiModel) -> Unit,
    val onBackClick: () -> Unit
) {
    companion object {
        val Preview = ServiceDetailsActions(
            onIntentClick = {},
            onBackClick = {}
        )
    }
}

@Preview
@Composable
private fun ServiceDetailsScreenPreview() {
    AppTheme(darkTheme = false) {
        ServiceDetailsScreen(
            data = ServiceDetailsData.Preview,
            actions = ServiceDetailsActions.Preview
        )
    }
}
