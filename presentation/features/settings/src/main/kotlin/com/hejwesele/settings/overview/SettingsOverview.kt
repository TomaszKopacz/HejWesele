package com.hejwesele.settings.overview

import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.HorizontalMargin
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.internet.InternetConnectionPopup
import com.hejwesele.settings.ISettingsFeatureNavigation
import com.hejwesele.settings.R
import com.ramcosta.composedestinations.annotation.Destination
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
@Destination
fun SettingsOverview(
    navigation: ISettingsFeatureNavigation
) {
    SettingsOverviewEntryPoint(navigation)
}

@Composable
private fun SettingsOverviewEntryPoint(
    navigation: ISettingsFeatureNavigation,
    viewModel: SettingsOverviewViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    }

    val uiState by viewModel.states.collectAsState()

    SettingsOverviewEventHandler(
        uiState = uiState,
        viewModel = viewModel,
        navigation = navigation
    )

    SettingsOverviewScreen(
        internetPopupEnabled = true,
        onBackClicked = { viewModel.onBack() },
        onTermsAndConditionClicked = { viewModel.onTermsAndConditionsRequested() },
        onPrivacyPolicyClicked = { viewModel.onPrivacyPolicyRequested() }
    )
}

@Composable
private fun SettingsOverviewEventHandler(
    uiState: SettingsOverviewUiState,
    viewModel: SettingsOverviewViewModel,
    navigation: ISettingsFeatureNavigation
) {
    EventEffect(
        event = uiState.navigateUp,
        onConsumed = { viewModel.onNavigatedUp() },
        action = { navigation.navigateUp() }
    )
    EventEffect(
        event = uiState.openTermsAndConditions,
        onConsumed = { viewModel.onTermsAndConditionsOpened() },
        action = { navigation.openTermsAndConditions() }
    )
    EventEffect(
        event = uiState.openPrivacyPolicy,
        onConsumed = { viewModel.onDataPrivacyOpened() },
        action = { navigation.openPrivacyPolicy() }
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
private fun SettingsOverviewScreen(
    internetPopupEnabled: Boolean,
    onBackClicked: () -> Unit,
    onTermsAndConditionClicked: () -> Unit,
    onPrivacyPolicyClicked: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            if (internetPopupEnabled) {
                InternetConnectionPopup(statusBarSensitive = false)
            }
            Column(
                modifier = Modifier
                    .weight(1.0f)
                    .padding(horizontal = Dimension.marginLarge)
            ) {
                VerticalMargin(padding.calculateTopPadding())
                Icon(
                    modifier = Modifier
                        .size(Dimension.iconNormal)
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onBackClicked() },
                    contentDescription = null,
                    painter = painterResource(R.drawable.ic_arrow_left),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                VerticalMargin(Dimension.marginLarge)
                Text(
                    text = Label.settingsTitleLabel,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                VerticalMargin(Dimension.marginExtraLarge)
                SettingsItem(
                    iconResId = R.drawable.ic_law,
                    label = Label.settingsTermsAndConditionsLabel,
                    onClick = onTermsAndConditionClicked
                )
                SettingsItem(
                    iconResId = R.drawable.ic_privacy,
                    label = Label.settingsPrivacyPolicyLabel,
                    onClick = onPrivacyPolicyClicked
                )
                VerticalMargin(Dimension.marginLarge)
            }
        }
    }
}

@Composable
private fun SettingsItem(
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = { onClick() })
            .padding(
                top = Dimension.marginExtraSmall,
                bottom = Dimension.marginExtraSmall,
                end = Dimension.marginExtraSmall
            ),
        shape = MaterialTheme.shapes.large
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.size(Dimension.iconNormal),
                tint = MaterialTheme.colorScheme.primary
            )
            HorizontalMargin(Dimension.marginNormal)
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
