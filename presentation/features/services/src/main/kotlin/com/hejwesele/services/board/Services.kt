package com.hejwesele.services.board

import android.annotation.SuppressLint
import androidx.annotation.RawRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.ErrorData
import com.hejwesele.android.components.ErrorView
import com.hejwesele.android.components.Loader
import com.hejwesele.android.components.TextPlaceholder
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.theme.Alpha
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.internet.InternetConnectionPopup
import com.hejwesele.services.IServicesNavigation
import com.hejwesele.services.model.MaterialServiceColor.PRIMARY
import com.hejwesele.services.model.MaterialServiceColor.SECONDARY
import com.hejwesele.services.model.MaterialServiceColor.TERTIARY
import com.hejwesele.services.model.ServiceListItem
import com.hejwesele.services.model.ServiceUiModel
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun Services(navigation: IServicesNavigation) = ServicesEntryPoint(
    navigation = navigation
)

@Composable
private fun ServicesEntryPoint(
    viewModel: ServicesViewModel = hiltViewModel(),
    navigation: IServicesNavigation
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    }

    val uiState by viewModel.states.collectAsState()
    val uiEvents by viewModel.events.collectAsState()

    ServicesEventHandler(
        events = uiEvents,
        viewModel = viewModel,
        navigation = navigation
    )

    val servicesData = with(uiState) {
        ServicesData(
            isLoading = isLoading,
            isEnabled = isEnabled,
            services = services,
            internetPopupEnabled = true,
            errorData = errorData
        )
    }

    val serviceActions = with(viewModel) {
        ServicesActions(
            onServiceSelected = { service -> onServiceSelected(service) } // TODO - replace with ::
        )
    }

    ServicesScreen(
        data = servicesData,
        actions = serviceActions
    )
}

@Composable
private fun ServicesEventHandler(
    events: ServicesUiEvents,
    viewModel: ServicesViewModel,
    navigation: IServicesNavigation
) {
    EventEffect(
        event = events.openServiceDetails,
        onConsumed = viewModel::onServiceDetailsOpened,
        action = { service -> navigation.openServiceDetails(
            title = service.title,
            name = service.name,
            description = service.description
        ) }
    )

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
private fun ServicesScreen(
    data: ServicesData,
    actions: ServicesActions
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
                    data.errorData != null -> ErrorView(modifier = Modifier.fillMaxSize())
                    !data.isEnabled -> TextPlaceholder(text = Label.scheduleDisabledMessageText)
                    else -> ServicesContent(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface),
                        services = data.services,
                        actions = actions
                    )
                }
            }
        }
    }
}

@Composable
private fun ServicesContent(
    modifier: Modifier = Modifier,
    services: List<ServiceListItem>,
    actions: ServicesActions
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = Dimension.marginSmall,
            end = Dimension.marginSmall,
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
            bottom = Dimension.marginNormal
        )
    ) {
        services.forEach { item ->
            item {
                when (item) {
                    is ServiceListItem.Label -> {
                        VerticalMargin(Dimension.marginLarge)
                        Text(
                            modifier = Modifier.padding(start = Dimension.marginSmall),
                            text = item.text,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    is ServiceListItem.Tile -> {
                        VerticalMargin(Dimension.marginNormal)
                        ServiceTile(
                            modifier = Modifier.fillMaxWidth(),
                            service = item.service,
                            onClick = { actions.onServiceSelected(item.service) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceTile(
    modifier: Modifier = Modifier,
    service: ServiceUiModel,
    onClick: () -> Unit
) {
    ServiceTileBackground(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ServiceTileDescription(
                modifier = Modifier.weight(Dimension.weightFull),
                service = service
            )
            ServiceTileAnimation(animationResId = service.animation)
        }
    }
}

@Composable
private fun ServiceTileBackground(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        Surface(
            Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = Dimension.elevationSmall,
                    shape = MaterialTheme.shapes.small
                )
                .clickable { onClick() },
            shape = MaterialTheme.shapes.small
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = Alpha.alpha25)
                            ),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    )
            ) {
                content()
            }
        }
    }
}

@Composable
private fun ServiceTileDescription(
    modifier: Modifier = Modifier,
    service: ServiceUiModel
) {
    val (backgroundColor, textColor) = with(MaterialTheme.colorScheme) {
        when (service.color) {
            PRIMARY -> errorContainer to onErrorContainer
            SECONDARY -> secondaryContainer to onSecondaryContainer
            TERTIARY -> tertiaryContainer to onTertiaryContainer
        }
    }

    Surface(
        modifier = modifier,
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Column(modifier = Modifier.padding(Dimension.marginNormal)) {
            Text(
                text = service.title,
                style = MaterialTheme.typography.headlineSmall,
                color = textColor
            )
            if (!service.name.isNullOrEmpty()) {
                VerticalMargin(Dimension.marginExtraSmall)
                Text(
                    text = service.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = textColor
                )
            }
            VerticalMargin(Dimension.marginNormal)
            Text(
                text = service.description,
                style = MaterialTheme.typography.bodySmall,
                color = textColor
            )
        }
    }
}

@Composable
private fun ServiceTileAnimation(
    modifier: Modifier = Modifier,
    @RawRes animationResId: Int
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(animationResId))

    Box(modifier = modifier) {
        LottieAnimation(
            modifier = Modifier.size(Dimension.iconOutsizeExtraLarge),
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
    }
}

private data class ServicesData(
    val isLoading: Boolean,
    val isEnabled: Boolean,
    val services: List<ServiceListItem>,
    val internetPopupEnabled: Boolean,
    val errorData: ErrorData?
) {
    companion object {
        val Preview = ServicesData(
            isLoading = false,
            isEnabled = true,
            services = emptyList(),
            internetPopupEnabled = false,
            errorData = null
        )
    }
}

private data class ServicesActions(
    val onServiceSelected: (ServiceUiModel) -> Unit
) {
    companion object {
        val Preview = ServicesActions(
            onServiceSelected = {}
        )
    }
}

@Preview
@Composable
private fun ServicesScreenPreview() {
    AppTheme(darkTheme = false) {
        ServicesScreen(
            data = ServicesData.Preview,
            actions = ServicesActions.Preview
        )
    }
}
