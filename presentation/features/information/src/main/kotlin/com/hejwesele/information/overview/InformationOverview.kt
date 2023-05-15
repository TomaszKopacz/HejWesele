package com.hejwesele.information.overview

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.HorizontalMargin
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.extensions.disabled
import com.hejwesele.information.IInformationFeatureNavigation
import com.hejwesele.internet.InternetConnectionPopup
import com.hejwesele.theme.R
import com.ramcosta.composedestinations.annotation.Destination
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
@Destination
fun InformationOverview(
    navigation: IInformationFeatureNavigation
) {
    InformationOverviewEntryPoint(navigation)
}

@Composable
private fun InformationOverviewEntryPoint(
    navigation: IInformationFeatureNavigation,
    viewModel: InformationOverviewViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    }

    val uiState by viewModel.states.collectAsState()
    val uiEvents by viewModel.events.collectAsState()

    InformationOverviewEventHandler(
        events = uiEvents,
        viewModel = viewModel,
        navigation = navigation
    )

    val data = with(uiState) {
        InformationOverviewData(
            contactEmail = contactEmail,
            appVersion = appVersion,
            internetPopupEnabled = true
        )
    }

    val actions = with(viewModel) {
        InformationOverviewActions(
            onBackClicked = ::onBack,
            onTermsAndConditionClicked = ::onTermsAndConditionsRequested,
            onPrivacyPolicyClicked = ::onPrivacyPolicyRequested
        )
    }

    InformationOverviewScreen(
        data = data,
        actions = actions
    )
}

@Composable
private fun InformationOverviewEventHandler(
    events: InformationOverviewUiEvents,
    viewModel: InformationOverviewViewModel,
    navigation: IInformationFeatureNavigation
) {
    EventEffect(
        event = events.navigateUp,
        onConsumed = viewModel::onNavigatedUp,
        action = navigation::navigateUp
    )
    EventEffect(
        event = events.openTermsAndConditions,
        onConsumed = viewModel::onTermsAndConditionsOpened,
        action = navigation::openTermsAndConditions
    )
    EventEffect(
        event = events.openPrivacyPolicy,
        onConsumed = viewModel::onDataPrivacyOpened,
        action = navigation::openPrivacyPolicy
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
private fun InformationOverviewScreen(
    data: InformationOverviewData,
    actions: InformationOverviewActions
) {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            if (data.internetPopupEnabled) {
                InternetConnectionPopup()
            }
            InformationOverviewContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(Dimension.weightFull)
                    .padding(horizontal = Dimension.marginLarge)
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                contactEmail = data.contactEmail,
                appVersion = data.appVersion,
                onBackClicked = actions.onBackClicked,
                onTermsAndConditionClicked = actions.onTermsAndConditionClicked,
                onPrivacyPolicyClicked = actions.onPrivacyPolicyClicked
            )
        }
    }
}

@Composable
private fun InformationOverviewContent(
    modifier: Modifier = Modifier,
    contactEmail: String,
    appVersion: String,
    onBackClicked: () -> Unit,
    onTermsAndConditionClicked: () -> Unit,
    onPrivacyPolicyClicked: () -> Unit
) {
    Column(modifier = modifier) {
        VerticalMargin(Dimension.marginNormal)
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
            text = Label.informationTitleLabel,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        VerticalMargin(Dimension.marginExtraLarge)
        InformationItem(
            iconResId = R.drawable.ic_law,
            label = Label.informationTermsAndConditionsLabel,
            onClick = onTermsAndConditionClicked
        )
        InformationItem(
            iconResId = R.drawable.ic_privacy,
            label = Label.informationPrivacyPolicyLabel,
            onClick = onPrivacyPolicyClicked
        )
        VerticalMargin(Dimension.marginLarge)
        Spacer(modifier = Modifier.weight(Dimension.weightFull))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "${Label.informationContactLabelText} $contactEmail",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.disabled
        )
        VerticalMargin(Dimension.marginExtraSmall)
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "${Label.informationAppVersionLabelText} $appVersion",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.disabled
        )
        VerticalMargin(Dimension.marginNormal)
    }
}

@Composable
private fun InformationItem(
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

private data class InformationOverviewData(
    val contactEmail: String,
    val appVersion: String,
    val internetPopupEnabled: Boolean
) {
    companion object {
        val Preview = InformationOverviewData(
            contactEmail = "fake.name@gmail.com",
            appVersion = "2.10.4 (1234)",
            internetPopupEnabled = false
        )
    }
}

private data class InformationOverviewActions(
    val onBackClicked: () -> Unit,
    val onTermsAndConditionClicked: () -> Unit,
    val onPrivacyPolicyClicked: () -> Unit
) {
    companion object {
        val Preview = InformationOverviewActions(
            onBackClicked = {},
            onTermsAndConditionClicked = {},
            onPrivacyPolicyClicked = {}
        )
    }
}

@Preview
@Composable
private fun InformationOverviewScreenPreview() {
    AppTheme(darkTheme = false) {
        InformationOverviewScreen(
            data = InformationOverviewData.Preview,
            actions = InformationOverviewActions.Preview
        )
    }
}
