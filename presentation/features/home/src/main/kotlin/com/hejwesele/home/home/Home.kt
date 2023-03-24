package com.hejwesele.home.home

import androidx.activity.compose.BackHandler
import androidx.annotation.RawRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.hejwesele.android.components.CircleImage
import com.hejwesele.android.components.ErrorView
import com.hejwesele.android.components.HorizontalMargin
import com.hejwesele.android.components.Loader
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
import com.hejwesele.home.home.model.IntentUiModel
import com.hejwesele.home.home.model.InvitationTileUiModel
import com.hejwesele.internet.InternetConnectionPopup
import com.hejwesele.invitations.model.InvitationTileType
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@Suppress("UnusedPrivateMember")
@Composable
fun Home(navigation: IHomeNavigation) = HomeEntryPoint()

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HomeEntryPoint(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.states.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    EventEffect(
        event = uiState.showTileOptions,
        onConsumed = { viewModel.onTileOptionsShown() },
        action = { sheetState.show() }
    )
    EventEffect(
        event = uiState.hideTileOptions,
        onConsumed = { viewModel.onTileOptionsHidden() },
        action = { sheetState.hide() }
    )
    EventEffect(
        event = uiState.openIntent,
        onConsumed = { viewModel.onIntentOpened() },
        action = { intent -> openActivity(context, intent.intentPackage, intent.url) }
    )

    HomeScreen(
        isLoading = uiState.isLoading,
        tiles = uiState.tiles,
        onTileClicked = { invitation ->
            coroutineScope.launch {
                viewModel.onTileClicked(invitation)
            }
        },
        sheetState = sheetState,
        intents = uiState.intents,
        onIntentSelected = { intent ->
            coroutineScope.launch {
                viewModel.onTileIntentOptionSelected(intent)
            }
        },
        isError = uiState.error != null,
        onErrorRetry = {
            coroutineScope.launch {
                viewModel.onErrorRetry()
            }
        },
        internetPopupEnabled = true
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }
}

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
private fun HomeScreen(
    isLoading: Boolean,
    tiles: List<InvitationTileUiModel>,
    onTileClicked: (InvitationTileUiModel) -> Unit,
    sheetState: ModalBottomSheetState,
    intents: List<IntentUiModel>,
    onIntentSelected: (IntentUiModel) -> Unit,
    isError: Boolean,
    onErrorRetry: () -> Unit,
    internetPopupEnabled: Boolean
) {
    BottomSheetScaffold(
        state = sheetState,
        sheetContent = {
            InvitationBottomSheetContent(
                intents = intents,
                onIntentSelected = onIntentSelected
            )
        }
    ) {
        when {
            isLoading -> Loader()
            isError -> ErrorView(
                onRetry = onErrorRetry
            )
            else -> HomeContent(
                tiles = tiles,
                onTileClicked = onTileClicked
            )
        }
        if (internetPopupEnabled) {
            InternetConnectionPopup()
        }
    }
}

@Composable
private fun HomeContent(
    tiles: List<InvitationTileUiModel>,
    onTileClicked: (InvitationTileUiModel) -> Unit
) {
    ScrollableColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        CoupleLottieAnimation()
        when {
            tiles.isEmpty() -> TextPlaceholder(
                text = Label.homeNoInvitationTilesMessage
            )
            else -> InvitationsTilesCarousel(
                tiles = tiles,
                onTileClicked = onTileClicked
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun InvitationsTilesCarousel(tiles: List<InvitationTileUiModel>, onTileClicked: (InvitationTileUiModel) -> Unit) {
    HorizontalPager(
        count = tiles.count(),
        contentPadding = PaddingValues(Dimension.marginLarge_3_4)
    ) { page ->
        InvitationTile(
            tile = tiles[page],
            onTileClicked = onTileClicked
        )
    }
}

@Composable
private fun InvitationTile(tile: InvitationTileUiModel, onTileClicked: (InvitationTileUiModel) -> Unit) {
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
                    avatars.isNotEmpty() -> InvitationTileAvatars(tile.avatars, tile.animationResId)
                    else -> InvitationTileLottieAnimation(tile.animationResId)
                }
                InvitationTileTitle(title, subtitle)
                InvitationTileDescription(description)
            }
        }
    }
}

@Composable
private fun InvitationTileTitle(title: String, subtitle: String?) {
    Row(
        Modifier.padding(bottom = Dimension.marginSmall),
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

@Suppress("MagicNumber")
@Composable
private fun InvitationTileDescription(text: String) {
    val linesCount = 4
    Text(
        text = text.addEmptyLines(linesCount),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = linesCount
    )
}

@Composable
private fun InvitationTileAvatars(avatars: List<String>, animationRes: Int) {
    if (avatars.isEmpty()) {
        InvitationTileLottieAnimation(animationRes = animationRes)
    } else {
        Row(
            Modifier
                .padding(bottom = Dimension.marginNormal)
                .fillMaxWidth()
        ) {
            avatars.forEachIndexed { index, url ->
                CircleImage(
                    url = url,
                    modifier = Modifier
                        .size(Dimension.imageSizeSmall)
                        .offset(-Dimension.marginSmall.times(index))
                )
            }
        }
    }
}

@Composable
private fun InvitationTileLottieAnimation(@RawRes animationRes: Int) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(animationRes))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier
            .padding(bottom = Dimension.marginNormal)
            .size(Dimension.imageSizeSmall)
            .aspectRatio(1.0f)
    )
}

@Composable
private fun CoupleLottieAnimation() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_wedding_couple))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier
            .padding(top = Dimension.marginLarge)
            .aspectRatio(1.0f)
    )
}

@Composable
private fun InvitationBottomSheetContent(intents: List<IntentUiModel>, onIntentSelected: (IntentUiModel) -> Unit) {
    intents.forEach { intent ->
        IntentItem(
            intent = intent,
            onClick = onIntentSelected
        )
        VerticalMargin(Dimension.marginNormal)
    }
}

@Composable
private fun IntentItem(
    intent: IntentUiModel,
    onClick: (IntentUiModel) -> Unit
) {
    Surface(
        Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onClick(intent) }
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = intent.iconResId),
                contentDescription = null,
                modifier = Modifier.size(Dimension.iconSizeNormal),
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

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
internal fun HomeScreenPreview() {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Expanded
    )

    AppTheme(darkTheme = false) {
        HomeScreen(
            isLoading = false,
            tiles = listOf(
                HomePreviewData.tile1,
                HomePreviewData.tile2
            ),
            onTileClicked = {},
            sheetState = sheetState,
            intents = listOf(
                HomePreviewData.intent1,
                HomePreviewData.intent2
            ),
            onIntentSelected = {},
            isError = false,
            onErrorRetry = {},
            internetPopupEnabled = false
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
