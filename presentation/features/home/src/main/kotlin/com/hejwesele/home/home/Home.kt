package com.hejwesele.home.home

import androidx.activity.compose.BackHandler
import androidx.annotation.RawRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.CircleImage
import com.hejwesele.android.components.HorizontalMargin
import com.hejwesele.android.components.Loader
import com.hejwesele.android.components.TextPlaceholder
import com.hejwesele.android.components.VerticalMargin
import com.hejwesele.android.components.layouts.BottomSheetScaffold
import com.hejwesele.android.components.layouts.ScrollableColumn
import com.hejwesele.android.theme.Dimension
import com.hejwesele.android.theme.Label
import com.hejwesele.extensions.addEmptyLines
import com.hejwesele.extensions.openActivity
import com.hejwesele.home.IHomeNavigation
import com.hejwesele.home.R
import com.hejwesele.home.home.model.IntentUiModel
import com.hejwesele.home.home.model.InvitationTileUiModel
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.launch

@Suppress("UnusedPrivateMember")
@Composable
fun Home(navigation: IHomeNavigation) = HomeScreen()

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
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

    BottomSheetScaffold(
        state = sheetState,
        sheetContent = {
            InvitationBottomSheetContent(
                intents = uiState.intents,
                onIntentSelected = { intent ->
                    coroutineScope.launch {
                        viewModel.onTileIntentOptionSelected(intent)
                    }
                }
            )
        }
    ) {
        when {
            uiState.isLoading -> Loader()
            uiState.error != null -> TextPlaceholder(text = Label.errorMessage)
            else -> HomeContent(
                tiles = uiState.tiles,
                onTileClicked = { invitation ->
                    coroutineScope.launch {
                        viewModel.onTileClicked(invitation)
                    }
                }
            )
        }
    }

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }
}

@Composable
private fun HomeContent(
    tiles: List<InvitationTileUiModel>,
    onTileClicked: (InvitationTileUiModel) -> Unit
) {
    ScrollableColumn {
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
                shape = RoundedCornerShape(Dimension.radiusRoundedCornerNormal)
            )
            .fillMaxWidth()
            .clickable(enabled = tile.clickable) {
                onTileClicked(tile)
            },
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(Dimension.radiusRoundedCornerNormal)
    ) {
        Column(
            Modifier
                .padding(Dimension.marginNormal)
                .fillMaxWidth()
        ) {
            with(tile) {
                when {
                    avatars.isNotEmpty() -> InvitationTileAvatars(tile)
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
private fun InvitationTileAvatars(tile: InvitationTileUiModel) {
    if (tile.avatars.isEmpty()) {
        InvitationTileLottieAnimation(animationRes = tile.animationResId)
    } else {
        Row(
            Modifier
                .padding(bottom = Dimension.marginNormal)
                .fillMaxWidth()
        ) {
            tile.avatars.forEachIndexed { index, url ->
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
