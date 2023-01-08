package com.miquido.androidtemplate.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.miquido.android.customtabs.LocalCustomTabs
import com.miquido.androidtemplate.onboarding.Constants.PRIVACY_POLICY_URL

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Onboarding(
    navController: NavHostController,
    onboardingViewModel: OnboardingViewModel = hiltViewModel()
) {
    val onboardingPages = OnboardingPages.values()
    val pagerState = rememberPagerState()
    val buttonsVisible = pagerState.currentPage == onboardingPages.lastIndex

    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            HorizontalPager(
                count = 3,
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) { position ->
                PagerScreen(onBoardingPage = onboardingPages[position])
            }
            HorizontalPagerIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                pagerState = pagerState
            )
            Link(
                modifier = Modifier
                    .padding(top = 20.dp),
                linkButtonVisible = buttonsVisible
            )
            NextButton(
                nextButtonVisible = buttonsVisible
            ) {
                onboardingViewModel.nextClick()
                navController.popBackStack()
            }
        }
    }
}

@Composable
private fun PagerScreen(onBoardingPage: OnboardingPages) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.5f)
                .fillMaxHeight(fraction = 0.6f),
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = onBoardingPage.title),
            fontSize = MaterialTheme.typography.h4.fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            text = stringResource(id = onBoardingPage.description),
            fontSize = MaterialTheme.typography.subtitle2.fontSize,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun Link(
    modifier: Modifier,
    linkButtonVisible: Boolean
) {
    val customTabs = LocalCustomTabs.current

    AnimatedVisibility(
        modifier = modifier
            .padding(horizontal = 24.dp),
        visible = linkButtonVisible
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { customTabs.launch(PRIVACY_POLICY_URL) },
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.privacy_policy_button_text),
                color = MaterialTheme.colors.primary,
                fontSize = MaterialTheme.typography.body2.fontSize
            )
            Icon(
                modifier = Modifier
                    .size(16.dp)
                    .align(alignment = Alignment.CenterVertically),
                imageVector = Icons.Filled.Link,
                tint = MaterialTheme.colors.primary,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun NextButton(
    nextButtonVisible: Boolean,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth(),
            visible = nextButtonVisible
        ) {
            Button(onClick = onNextClick) {
                Text(text = stringResource(id = R.string.next_button_text))
            }
        }
    }
}
