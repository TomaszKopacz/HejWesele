package com.hejwesele.home

import androidx.annotation.RawRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hejwesele.android.components.CircleImage
import com.hejwesele.android.components.Loader
import com.hejwesele.android.components.ScrollableColumn
import com.hejwesele.android.components.TextPlaceholder
import com.hejwesele.android.theme.Dimension
import com.hejwesele.extensions.addEmptyLines
import com.hejwesele.home.constants.Numbers
import com.hejwesele.home.constants.Strings
import com.hejwesele.home.model.HomeTileUiModel
import com.hejwesele.model.home.HomeTileType

@Composable
internal fun Home(
    eventId: Int,
    viewModel: HomeViewModel
) {
    val systemUiController = rememberSystemUiController()
    SideEffect { systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true) }

    viewModel.init().also {
        HomeContent(viewModel)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeContent(viewModel: HomeViewModel) {
    val uiState by viewModel.states.collectAsState()

    when {
        uiState.isLoading -> Loader()
        uiState.tiles.isEmpty() -> TextPlaceholder(text = Strings.noHomeTilesText)
        else -> ScrollableColumn {
            CoupleLottieAnimation()
            HomeTilesCarousel(tiles = uiState.tiles)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun HomeTilesCarousel(tiles: List<HomeTileUiModel>) {
    HorizontalPager(
        count = tiles.count(),
        contentPadding = PaddingValues(Dimension.marginLarge_3_4)
    ) { page ->
        HomeTile(tile = tiles[page])
    }
}

@Composable
private fun HomeTile(tile: HomeTileUiModel) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(Dimension.radiusRoundedCornerNormal),
        modifier = Modifier
            .padding(horizontal = Dimension.marginLarge_1_4)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(Dimension.radiusRoundedCornerNormal))
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(Dimension.marginNormal)
                .fillMaxWidth()
        ) {
            with(tile) {
                when {
                    photoUrls.isNotEmpty() -> HomeTilePhotos(type, photoUrls)
                    else -> HomeTileLottieAnimation(getAnimationResource(type))
                }
                HomeTileTitle(title, subtitle)
                HomeTileDescription(description)
            }
        }
    }
}

@Composable
private fun HomeTileTitle(title: String, subtitle: String?) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.padding(bottom = Dimension.marginSmall),
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
private fun HomeTileDescription(text: String) {
    val linesCount = Numbers.homeTileDescriptionLinesCount
    Text(
        text = text.addEmptyLines(linesCount),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = linesCount
    )
}

@Composable
private fun HomeTilePhotos(type: HomeTileType, photoUrls: List<String>) {
    if (photoUrls.isEmpty()) {
        HomeTileLottieAnimation(animationRes = getAnimationResource(type))
    } else {
        Row(
            modifier = Modifier
                .padding(bottom = Dimension.marginNormal)
                .fillMaxWidth()
        ) {
            photoUrls.forEachIndexed { index, url ->
                CircleImage(
                    url = url,
                    size = Dimension.imageSizeSmall,
                    offset = -Dimension.marginSmall.times(index)
                )
            }
        }
    }
}

@Composable
private fun HomeTileLottieAnimation(@RawRes animationRes: Int) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(animationRes))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier
            .padding(bottom = Dimension.marginNormal)
            .size(Dimension.imageSizeSmall)
            .aspectRatio(Numbers.homeAnimationsRatio)
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
            .aspectRatio(Numbers.homeAnimationsRatio)
    )
}

private fun getAnimationResource(tileType: HomeTileType) = when (tileType) {
    HomeTileType.COUPLE -> R.raw.lottie_heart
    HomeTileType.DATE -> R.raw.lottie_calendar
    HomeTileType.CHURCH -> R.raw.lottie_church
    HomeTileType.VENUE -> R.raw.lottie_home
    HomeTileType.WISHES -> R.raw.lottie_toast
}

