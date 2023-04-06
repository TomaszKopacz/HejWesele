package com.hejwesele.settings.privacypolicy

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
import com.hejwesele.regulations.model.RegulationPoint
import com.hejwesele.regulations.model.RegulationPointType.LETTER_POINT
import com.hejwesele.regulations.model.RegulationPointType.NUMBER_POINT
import com.hejwesele.regulations.model.RegulationPointType.PARAGRAPH
import com.hejwesele.regulations.model.RegulationPointType.TITLE
import com.hejwesele.settings.ISettingsFeatureNavigation
import com.hejwesele.settings.R
import com.hejwesele.settings.ui.RegulationItem
import com.ramcosta.composedestinations.annotation.Destination
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
@Destination
fun PrivacyPolicy(
    navigation: ISettingsFeatureNavigation
) {
    PrivacyPolicyEntryPoint(navigation)
}

@Composable
private fun PrivacyPolicyEntryPoint(
    navigation: ISettingsFeatureNavigation,
    viewModel: PrivacyPolicyViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    }

    val uiState by viewModel.states.collectAsState()

    PrivacyPolicyEventHandler(
        uiState = uiState,
        viewModel = viewModel,
        navigation = navigation
    )

    PrivacyPolicyScreen(
        isLoading = uiState.isLoading,
        isError = uiState.isError,
        internetPopupEnabled = true,
        regulations = uiState.regulations,
        onBackCLicked = { viewModel.onBack() }
    )
}

@Composable
private fun PrivacyPolicyEventHandler(
    uiState: PrivacyPolicyUiState,
    viewModel: PrivacyPolicyViewModel,
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
private fun PrivacyPolicyScreen(
    isLoading: Boolean,
    isError: Boolean,
    internetPopupEnabled: Boolean,
    regulations: List<RegulationPoint>,
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
                    regulations.isNotEmpty() ->
                        PrivacyPolicyContent(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.surface),
                            padding = padding,
                            regulations = regulations,
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
private fun PrivacyPolicyContent(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    regulations: List<RegulationPoint>,
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
                VerticalMargin(Dimension.marginNormal)
            }
            regulations.forEach { regulationPoint ->
                item {
                    RegulationItem(
                        regulationPoint = regulationPoint,
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
private fun PrivacyPolicyScreenPreview() {
    AppTheme(darkTheme = false) {
        PrivacyPolicyScreen(
            isLoading = false,
            isError = false,
            internetPopupEnabled = false,
            regulations = previewRegulations,
            onBackCLicked = {}
        )
    }
}

private val previewRegulations = listOf(
    RegulationPoint(
        type = TITLE,
        level = 0,
        order = null,
        text = "Title"
    ),
    RegulationPoint(
        type = PARAGRAPH,
        level = 0,
        order = "1",
        text = "Paragraph"
    ),
    RegulationPoint(
        type = NUMBER_POINT,
        level = 1,
        order = "1",
        text = "Definitions"
    ),
    RegulationPoint(
        type = LETTER_POINT,
        level = 2,
        order = "a",
        text = "Application"
    ),
    RegulationPoint(
        type = LETTER_POINT,
        level = 2,
        order = "b",
        text = "User"
    )
)
