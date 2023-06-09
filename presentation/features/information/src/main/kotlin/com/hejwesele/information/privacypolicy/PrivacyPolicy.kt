package com.hejwesele.information.privacypolicy

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.ErrorData
import com.hejwesele.android.components.ErrorView
import com.hejwesele.android.components.Loader
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.information.IInformationFeatureNavigation
import com.hejwesele.information.ui.LegalPointItem
import com.hejwesele.internet.InternetConnectionPopup
import com.hejwesele.legaldocument.LegalPoint
import com.hejwesele.legaldocument.LegalPointType.LETTER_POINT
import com.hejwesele.legaldocument.LegalPointType.NUMBER_POINT
import com.hejwesele.legaldocument.LegalPointType.PARAGRAPH
import com.hejwesele.legaldocument.LegalPointType.TITLE
import com.hejwesele.theme.R
import com.ramcosta.composedestinations.annotation.Destination
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
@Destination
fun PrivacyPolicy(
    navigation: IInformationFeatureNavigation
) {
    PrivacyPolicyEntryPoint(navigation)
}

@Composable
private fun PrivacyPolicyEntryPoint(
    navigation: IInformationFeatureNavigation,
    viewModel: PrivacyPolicyViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    }

    val uiState by viewModel.states.collectAsState()
    val uiEvents by viewModel.events.collectAsState()

    PrivacyPolicyEventHandler(
        events = uiEvents,
        viewModel = viewModel,
        navigation = navigation
    )

    val data = with(uiState) {
        PrivacyPolicyData(
            isLoading = isLoading,
            legalPoints = legalPoints,
            internetPopupEnabled = true,
            error = error
        )
    }

    val actions = with(viewModel) {
        PrivacyPolicyActions(
            onBackClicked = ::onBack
        )
    }

    PrivacyPolicyScreen(
        data = data,
        actions = actions
    )
}

@Composable
private fun PrivacyPolicyEventHandler(
    events: PrivacyPolicyUiEvents,
    viewModel: PrivacyPolicyViewModel,
    navigation: IInformationFeatureNavigation
) {
    EventEffect(
        event = events.navigateUp,
        onConsumed = viewModel::onNavigatedUp,
        action = navigation::navigateUp
    )
}

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalCoroutinesApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
private fun PrivacyPolicyScreen(
    data: PrivacyPolicyData,
    actions: PrivacyPolicyActions
) {
    Scaffold { padding ->
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
                    data.legalPoints.isNotEmpty() ->
                        PrivacyPolicyContent(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.surface),
                            padding = padding,
                            legalPoints = data.legalPoints,
                            onBackClicked = actions.onBackClicked
                        )
                    data.isLoading -> Loader()
                    data.error != null -> ErrorView(data = data.error)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PrivacyPolicyContent(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    legalPoints: List<LegalPoint>,
    onBackClicked: () -> Unit
) {
    val typography = MaterialTheme.typography

    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        LazyColumn(
            modifier = modifier
                .padding(
                    start = Dimension.marginLarge,
                    end = Dimension.marginLarge
                )
        ) {
            item {
                VerticalMargin(padding.calculateTopPadding() + Dimension.marginNormal)
            }
            item {
                Icon(
                    modifier = Modifier
                        .size(Dimension.iconNormal)
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onBackClicked() },
                    contentDescription = null,
                    painter = painterResource(R.drawable.ic_arrow_left),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            item {
                VerticalMargin(Dimension.marginNormal)
            }
            legalPoints.forEach { legalPoint ->
                item {
                    LegalPointItem(
                        legalPoint = legalPoint,
                        typography = typography
                    )
                }
            }
            item {
                VerticalMargin(padding.calculateBottomPadding())
            }
        }
    }
}

private data class PrivacyPolicyData(
    val isLoading: Boolean,
    val legalPoints: List<LegalPoint>,
    val internetPopupEnabled: Boolean,
    val error: ErrorData?
) {
    companion object {
        val Preview = PrivacyPolicyData(
            isLoading = false,
            internetPopupEnabled = false,
            legalPoints = previewLegalPoints,
            error = null
        )
    }
}

private data class PrivacyPolicyActions(
    val onBackClicked: () -> Unit
) {
    companion object {
        val Preview = PrivacyPolicyActions(
            onBackClicked = {}
        )
    }
}

private val previewLegalPoints = listOf(
    LegalPoint(
        type = TITLE,
        level = "0",
        order = null,
        text = "Title"
    ),
    LegalPoint(
        type = PARAGRAPH,
        level = "0",
        order = "1",
        text = "Paragraph"
    ),
    LegalPoint(
        type = NUMBER_POINT,
        level = "1",
        order = "1",
        text = "Definitions"
    ),
    LegalPoint(
        type = LETTER_POINT,
        level = "2",
        order = "a",
        text = "Application"
    ),
    LegalPoint(
        type = LETTER_POINT,
        level = "2",
        order = "b",
        text = "User"
    )
)

@Preview
@Composable
private fun PrivacyPolicyScreenPreview() {
    AppTheme(darkTheme = false) {
        PrivacyPolicyScreen(
            data = PrivacyPolicyData.Preview,
            actions = PrivacyPolicyActions.Preview
        )
    }
}
