package com.hejwesele.home.home

import androidx.activity.compose.BackHandler
import androidx.annotation.RawRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.AlertData
import com.hejwesele.android.components.AlertDialog
import com.hejwesele.android.components.CircleImage
import com.hejwesele.android.components.ErrorData
import com.hejwesele.android.components.ErrorView
import com.hejwesele.android.components.HorizontalMargin
import com.hejwesele.android.components.Loader
import com.hejwesele.android.components.LoaderDialog
import com.hejwesele.android.components.MenuComponent
import com.hejwesele.android.components.MenuItemData
import com.hejwesele.android.components.TextPlaceholder
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.components.layouts.BottomSheetScaffold
import com.hejwesele.android.components.layouts.ScrollableColumn
import com.hejwesele.android.theme.AppTheme
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.extensions.addEmptyLines
import com.hejwesele.extensions.openActivity
import com.hejwesele.home.IHomeNavigation
import com.hejwesele.home.R
import com.hejwesele.home.home.model.Constants
import com.hejwesele.home.home.model.IntentUiModel
import com.hejwesele.home.home.model.InvitationTileUiModel
import com.hejwesele.internet.InternetConnectionPopup
import com.hejwesele.invitations.model.InvitationTileType
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@Composable
fun Home(navigation: IHomeNavigation) = HomeEntryPoint(navigation = navigation)

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HomeEntryPoint(
    viewModel: HomeViewModel = hiltViewModel(),
    navigation: IHomeNavigation
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    }

    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.states.collectAsState()
    val uiEvents by viewModel.events.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    HomeEventHandler(
        events = uiEvents,
        viewModel = viewModel,
        navigation = navigation,
        sheetState = sheetState,
        coroutineScope = coroutineScope
    )

    val homeData = with(uiState) {
        HomeData(
            isLoading = isLoading,
            isEnabled = isEnabled,
            isLoggingOut = isLoggingOut,
            tiles = tiles,
            intents = intents,
            errorData = errorData,
            alertData = alertData,
            internetPopupEnabled = true
        )
    }

    val homeActions = HomeActions(
        onInformationItemClicked = {
            coroutineScope.launch {
                viewModel.onInformationRequested()
            }
        },
        onLogoutItemClicked = {
            coroutineScope.launch {
                viewModel.onLogoutRequested()
            }
        },
        onTileClicked = { invitation ->
            coroutineScope.launch {
                viewModel.onTileSelected(invitation)
            }
        },
        onIntentSelected = { intent ->
            coroutineScope.launch {
                viewModel.onTileIntentOptionSelected(intent)
            }
        }
    )

    HomeScreen(
        data = homeData,
        actions = homeActions,
        sheetState = sheetState
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HomeEventHandler(
    events: HomeUiEvents,
    viewModel: HomeViewModel,
    navigation: IHomeNavigation,
    sheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope
) {
    val context = LocalContext.current

    EventEffect(
        event = events.openInformation,
        onConsumed = { viewModel.onInformationOpened() },
        action = { navigation.openInformation() }
    )
    EventEffect(
        event = events.showTileOptions,
        onConsumed = { viewModel.onTileOptionsShown() },
        action = { sheetState.show() }
    )
    EventEffect(
        event = events.hideTileOptions,
        onConsumed = { viewModel.onTileOptionsHidden() },
        action = { sheetState.hide() }
    )
    EventEffect(
        event = events.openIntent,
        onConsumed = { viewModel.onIntentOpened() },
        action = { intent -> openActivity(context, intent.intentPackage, intent.url) }
    )
    EventEffect(
        event = events.openLogin,
        onConsumed = { viewModel.onLoginOpened() },
        action = { navigation.openLogin() }
    )
    BackHandler(enabled = sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }
    BackHandler(enabled = !sheetState.isVisible) {
        coroutineScope.launch { navigation.finishApplication() }
    }
}

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalCoroutinesApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
private fun HomeScreen(
    data: HomeData,
    actions: HomeActions,
    sheetState: ModalBottomSheetState
) {
    BottomSheetScaffold(
        state = sheetState,
        padding = PaddingValues(
            horizontal = Dimension.marginLarge,
            vertical = Dimension.marginNormal
        ),
        sheetContent = {
            InvitationBottomSheetContent(
                intents = data.intents,
                onIntentSelected = actions.onIntentSelected
            )
        }
    ) {
        Scaffold { padding ->
            Column(modifier = Modifier.fillMaxSize()) {
                if (data.internetPopupEnabled) {
                    InternetConnectionPopup(statusBarSensitive = false)
                }
                Box(
                    modifier = Modifier
                        .weight(1.0f)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    when {
                        data.isLoading -> Loader()
                        !data.isEnabled -> TextPlaceholder(text = Label.homeDisabledMessageText)
                        data.errorData != null -> ErrorView(modifier = Modifier.fillMaxSize())
                        else -> HomeContent(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(top = padding.calculateTopPadding()),
                            tiles = data.tiles,
                            onInformationItemClicked = actions.onInformationItemClicked,
                            onLogoutItemClicked = actions.onLogoutItemClicked,
                            onTileClicked = actions.onTileClicked
                        )
                    }
                }
            }
            if (data.isLoggingOut) {
                LoaderDialog(label = Label.homeLoggingOutText)
            }
            if (data.alertData != null) {
                AlertDialog(data = data.alertData)
            }
        }
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    tiles: List<InvitationTileUiModel>,
    onLogoutItemClicked: () -> Unit,
    onInformationItemClicked: () -> Unit,
    onTileClicked: (InvitationTileUiModel) -> Unit
) {
    ScrollableColumn(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth()) {
            CoupleLottieAnimation(modifier = Modifier.aspectRatio(1.0f))
            MenuComponent(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(Dimension.marginNormal),
                iconResId = R.drawable.ic_options,
                items = listOf(
                    MenuItemData(text = Label.homeMenuItemInformationText, action = onInformationItemClicked),
                    MenuItemData(text = Label.homeMenuItemLogoutText, action = onLogoutItemClicked)
                )
            )
        }
        when {
            tiles.isEmpty() -> TextPlaceholder(
                text = Label.homeNoInvitationTilesText
            )
            else -> InvitationsTilesCarousel(
                tiles = tiles,
                onTileClicked = onTileClicked
            )
        }
    }
}

@Composable
private fun CoupleLottieAnimation(
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_wedding_couple))
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun InvitationsTilesCarousel(
    modifier: Modifier = Modifier,
    tiles: List<InvitationTileUiModel>,
    onTileClicked: (InvitationTileUiModel) -> Unit
) {
    HorizontalPager(
        modifier = modifier,
        count = tiles.count(),
        contentPadding = PaddingValues(Dimension.marginLarge_3_4)
    ) { page ->
        InvitationTile(
            modifier = Modifier.fillMaxWidth(),
            tile = tiles[page],
            onTileClicked = onTileClicked
        )
    }
}

@Composable
private fun InvitationTile(
    modifier: Modifier = Modifier,
    tile: InvitationTileUiModel,
    onTileClicked: (InvitationTileUiModel) -> Unit
) {
    Box(modifier = modifier) {
        Surface(
            Modifier
                .padding(horizontal = Dimension.marginLarge_1_4)
                .shadow(
                    elevation = Dimension.elevationSmall,
                    shape = MaterialTheme.shapes.small
                )
                .fillMaxWidth()
                .clickable(enabled = tile.clickable) {
                    onTileClicked(tile)
                },
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.small
        ) {
            Column(
                Modifier
                    .padding(Dimension.marginNormal)
                    .fillMaxWidth()
            ) {
                with(tile) {
                    when {
                        avatars.isNotEmpty() -> InvitationTileAvatars(
                            modifier = Modifier.fillMaxWidth(),
                            avatars = tile.avatars,
                            animationResId = tile.animationResId
                        )
                        else -> InvitationTileLottieAnimation(
                            modifier = Modifier
                                .padding(bottom = Dimension.marginNormal)
                                .size(Dimension.imageSmall)
                                .aspectRatio(1.0f),
                            animationResId = tile.animationResId
                        )
                    }
                    InvitationTileTitle(
                        modifier = Modifier.padding(bottom = Dimension.marginSmall),
                        title = title,
                        subtitle = subtitle
                    )
                    InvitationTileDescription(text = description)
                }
            }
        }
    }
}

@Composable
private fun InvitationTileTitle(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String?
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = Dimension.marginSmall)
            )
        }
    }
}

@Composable
private fun InvitationTileDescription(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text.addEmptyLines(Constants.HomeTileLinesCount),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = Constants.HomeTileLinesCount
    )
}

@Composable
private fun InvitationTileAvatars(
    modifier: Modifier = Modifier,
    avatars: List<String>,
    animationResId: Int
) {
    Box(modifier = modifier)
    if (avatars.isEmpty()) {
        InvitationTileLottieAnimation(
            modifier = Modifier
                .padding(bottom = Dimension.marginNormal)
                .size(Dimension.imageSmall)
                .aspectRatio(1.0f),
            animationResId = animationResId
        )
    } else {
        Row(
            Modifier
                .padding(bottom = Dimension.marginNormal)
                .fillMaxWidth()
        ) {
            avatars.forEachIndexed { index, url ->
                CircleImage(
                    modifier = Modifier
                        .size(Dimension.imageSmall)
                        .offset(-Dimension.marginSmall.times(index)),
                    url = url,
                    loader = {},
                    fallback = {}
                )
            }
        }
    }
}

@Composable
private fun InvitationTileLottieAnimation(
    modifier: Modifier = Modifier,
    @RawRes animationResId: Int
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(animationResId))
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}

@Composable
private fun InvitationBottomSheetContent(intents: List<IntentUiModel>, onIntentSelected: (IntentUiModel) -> Unit) {
    fun Int.isLastItem() = this == intents.size - 1

    intents.forEachIndexed { index, intent ->
        IntentItem(
            modifier = Modifier.fillMaxWidth(),
            intent = intent,
            onClick = onIntentSelected
        )
        if (!index.isLastItem()) {
            VerticalMargin(Dimension.marginNormal)
        }
    }
}

@Composable
private fun IntentItem(
    modifier: Modifier = Modifier,
    intent: IntentUiModel,
    onClick: (IntentUiModel) -> Unit
) {
    Surface(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { onClick(intent) }
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = intent.iconResId),
                contentDescription = null,
                modifier = Modifier.size(Dimension.iconNormal),
                tint = MaterialTheme.colorScheme.primary
            )
            HorizontalMargin(Dimension.marginNormal)
            Text(
                text = intent.title,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private data class HomeData(
    val isLoading: Boolean,
    val isEnabled: Boolean,
    val isLoggingOut: Boolean,
    val tiles: List<InvitationTileUiModel>,
    val intents: List<IntentUiModel>,
    val internetPopupEnabled: Boolean,
    val errorData: ErrorData?,
    val alertData: AlertData?
) {
    companion object {
        val Preview = HomeData(
            isLoading = false,
            isEnabled = true,
            isLoggingOut = false,
            tiles = listOf(
                HomePreviewData.tile1,
                HomePreviewData.tile2
            ),
            intents = listOf(
                HomePreviewData.intent1,
                HomePreviewData.intent2
            ),
            errorData = null,
            alertData = null,
            internetPopupEnabled = false
        )
    }
}

private data class HomeActions(
    val onInformationItemClicked: () -> Unit,
    val onLogoutItemClicked: () -> Unit,
    val onTileClicked: (InvitationTileUiModel) -> Unit,
    val onIntentSelected: (IntentUiModel) -> Unit
) {
    companion object {
        val Preview = HomeActions(
            onInformationItemClicked = {},
            onLogoutItemClicked = {},
            onTileClicked = {},
            onIntentSelected = {}
        )
    }
}

private object HomePreviewData {
    val intent1 = IntentUiModel(
        title = "mr_groom",
        iconResId = R.drawable.ic_instagram_primary,
        intentPackage = null,
        url = "fake url"
    )

    val intent2 = IntentUiModel(
        title = "mrs_bride",
        iconResId = R.drawable.ic_instagram_primary,
        intentPackage = null,
        url = "fake url"
    )

    val tile1 = InvitationTileUiModel(
        type = InvitationTileType.COUPLE,
        title = "Wedding couple",
        subtitle = "Mr & Mrs Smith",
        description = "Hello!, Welcome to our wedding. Thank you for joining us this day, have fun!",
        avatars = listOf("fake url 1", "fake url 2"),
        animationResId = R.raw.lottie_heart,
        intents = listOf(intent1, intent2),
        clickable = false
    )

    val tile2 = InvitationTileUiModel(
        type = InvitationTileType.CHURCH,
        title = "Church",
        subtitle = "",
        description = "This is our adorable church. Amen.",
        avatars = emptyList(),
        animationResId = R.raw.lottie_heart,
        intents = listOf(intent1, intent2),
        clickable = false
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun HomeScreenPreview() {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Expanded
    )

    AppTheme(darkTheme = false) {
        HomeScreen(
            data = HomeData.Preview,
            actions = HomeActions.Preview,
            sheetState = sheetState
        )
    }
}

@Preview
@Composable
private fun InvitationTilePreview() {
    AppTheme(darkTheme = false) {
        InvitationTile(
            tile = HomePreviewData.tile2,
            onTileClicked = {}
        )
    }
}
