package com.hejwesele.settings.termsandconditions

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
import com.hejwesele.android.components.ErrorView
import com.hejwesele.android.components.Loader
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.internet.InternetConnectionPopup
import com.hejwesele.legaldocument.LegalPoint
import com.hejwesele.legaldocument.LegalPointType
import com.hejwesele.settings.ISettingsFeatureNavigation
import com.hejwesele.settings.R
import com.hejwesele.settings.ui.LegalPointItem
import com.ramcosta.composedestinations.annotation.Destination
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
@Destination
fun TermsAndConditions(
    navigation: ISettingsFeatureNavigation
) {
    TermsAndConditionsEntryPoint(navigation)
}

@Composable
private fun TermsAndConditionsEntryPoint(
    navigation: ISettingsFeatureNavigation,
    viewModel: TermsAndConditionsViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    }

    val uiState by viewModel.states.collectAsState()

    TermsAndConditionsEventHandler(
        uiState = uiState,
        viewModel = viewModel,
        navigation = navigation
    )

    TermsAndConditionsScreen(
        isLoading = uiState.isLoading,
        isError = uiState.isError,
        internetPopupEnabled = true,
        legalPoints = uiState.points,
        onBackCLicked = { viewModel.onBack() }
    )
}

@Composable
private fun TermsAndConditionsEventHandler(
    uiState: TermsAndConditionsUiState,
    viewModel: TermsAndConditionsViewModel,
    navigation: ISettingsFeatureNavigation
) {
    EventEffect(
        event = uiState.navigateUp,
        onConsumed = { viewModel.onNavigatedUp() },
        action = { navigation.navigateUp() }
    )
}

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalCoroutinesApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
private fun TermsAndConditionsScreen(
    isLoading: Boolean,
    isError: Boolean,
    internetPopupEnabled: Boolean,
    legalPoints: List<LegalPoint>,
    onBackCLicked: () -> Unit
) {
    Scaffold { padding ->
        Column(modifier = Modifier.fillMaxSize()) {
            if (internetPopupEnabled) {
                InternetConnectionPopup(statusBarSensitive = false)
            }
            Box(
                modifier = Modifier
                    .weight(1.0f)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                when {
                    legalPoints.isNotEmpty() ->
                        TermsAndConditionsContent(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.surface),
                            padding = padding,
                            legalPoints = legalPoints,
                            onBackClicked = onBackCLicked
                        )
                    isLoading -> Loader()
                    isError -> ErrorView()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TermsAndConditionsContent(
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
                VerticalMargin(padding.calculateTopPadding())
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
                VerticalMargin(Dimension.marginLarge)
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

@Preview
@Composable
private fun TermsAndConditionsScreenPreview() {
    AppTheme(darkTheme = false) {
        TermsAndConditionsScreen(
            isLoading = false,
            isError = false,
            internetPopupEnabled = false,
            legalPoints = previewLegalPoints,
            onBackCLicked = {}
        )
    }
}

private val previewLegalPoints = listOf(
    LegalPoint(
        type = LegalPointType.TITLE,
        level = "0",
        order = "null",
        text = "Title"
    ),
    LegalPoint(
        type = LegalPointType.PARAGRAPH,
        level = "0",
        order = "1",
        text = "Paragraph"
    ),
    LegalPoint(
        type = LegalPointType.NUMBER_POINT,
        level = "1",
        order = "1",
        text = "Definitions"
    ),
    LegalPoint(
        type = LegalPointType.LETTER_POINT,
        level = "2",
        order = "a",
        text = "Application"
    ),
    LegalPoint(
        type = LegalPointType.LETTER_POINT,
        level = "2",
        order = "b",
        text = "User"
    )
)
