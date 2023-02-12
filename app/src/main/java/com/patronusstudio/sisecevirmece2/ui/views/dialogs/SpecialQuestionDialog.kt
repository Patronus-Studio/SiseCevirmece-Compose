package com.patronusstudio.sisecevirmece2.ui.views.dialogs

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.patronusstudio.sisecevirmece2.BuildConfig
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.data.enums.AnimMillis
import com.patronusstudio.sisecevirmece2.data.enums.InterstitialAdViewLoadStatusEnum
import com.patronusstudio.sisecevirmece2.data.utils.getActivity
import com.patronusstudio.sisecevirmece2.data.utils.multiEventSend
import com.patronusstudio.sisecevirmece2.data.utils.showLog
import com.patronusstudio.sisecevirmece2.data.utils.showSample
import com.patronusstudio.sisecevirmece2.data.viewModels.SpecialGameScreenViewModel
import com.patronusstudio.sisecevirmece2.ui.theme.AppColor
import com.patronusstudio.sisecevirmece2.ui.widgets.AutoTextSize
import com.patronusstudio.sisecevirmece2.ui.widgets.InterstitialAdView
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.rememberFlipController
import kotlinx.coroutines.launch

@Composable
fun SpecialQuestionDialog(
    mixpanelAPI: MixpanelAPI,
    closeClicked: () -> Unit,
    viewModel: SpecialGameScreenViewModel,

    ) {
    val localContext = LocalContext.current
    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp
    val smallCardHeight = (height * 0.06).dp
    val smallPaddingHeight = (height * 0.03).dp
    val coroutineScope = rememberCoroutineScope()
    val flipController = rememberFlipController()
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        TitleCard(
            (width * 0.9).dp, viewModel.randomPackage.value?.packageName ?: stringResource(
                id = R.string.play_special_title
            )
        )
        Flippable(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
            frontSide = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    GeneralCard(
                        (width * 0.9).dp, (height * 0.2).dp,
                        text = viewModel.randomQuestion.value?.question ?: "",
                        cardPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(smallPaddingHeight))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        GeneralCard(
                            width = (width * 0.45).dp,
                            smallCardHeight,
                            text = stringResource(R.string.replied),
                            clicked = {
                                sendDataToMixApi(
                                    viewModel, localContext, mixpanelAPI,
                                    localContext.getString(R.string.replied)
                                )
                                closeClicked()
                            }
                        )
                        GeneralCard(
                            width = (width * 0.45).dp,
                            smallCardHeight,
                            text = stringResource(R.string.change_question),
                            clicked = {
                                val random = (0..10).random()
                                showLog(random.toString())
                                if (random < 9) {
                                    coroutineScope.launch {
                                        viewModel.getRandomPackage()
                                        viewModel.getRandomQuestion()
                                    }
                                } else {
                                    viewModel.setLoadingStatus(true)
                                    InterstitialAdView.loadInterstitial(
                                        localContext.getActivity(),
                                        BuildConfig.special_game_interstitial
                                    ) { ad ->
                                        when (ad) {
                                            InterstitialAdViewLoadStatusEnum.SHOWED -> {
                                                viewModel.setLoadingStatus(false)
                                            }
                                            InterstitialAdViewLoadStatusEnum.DISSMISSED -> {
                                                viewModel.setLoadingStatus(false)
                                                coroutineScope.launch {
                                                    viewModel.getRandomPackage()
                                                    viewModel.getRandomQuestion()
                                                }
                                            }
                                            else -> localContext.showSample()
                                        }
                                    }
                                }
                                sendDataToMixApi(
                                    viewModel, localContext, mixpanelAPI,
                                    localContext.getString(R.string.change_question)
                                )
                            })
                    }
                    Spacer(modifier = Modifier.height(smallPaddingHeight))
                    GeneralCard(
                        (width * 0.9).dp,
                        smallCardHeight,
                        text = stringResource(R.string.show_answer),
                        clicked = {
                            flipController.flip()
                            sendDataToMixApi(
                                viewModel, localContext, mixpanelAPI,
                                localContext.getString(R.string.show_answer)
                            )
                            closeClicked()
                        }
                    )
                }
            },
            backSide = {
                Box(modifier = Modifier
                    .size(400.dp)
                    .background(AppColor.White).clickable {  })
            },
            flipController = flipController,
            flipOnTouch = false,
            flipDurationMs = AnimMillis.NORMAL.millis
        )
    }
}

@Composable
private fun TitleCard(width: Dp, title: String) {
    Card(
        modifier = Modifier
            .width(width)
            .padding(vertical = 8.dp), backgroundColor = AppColor.Mustard,
        shape = RoundedCornerShape(16.dp), elevation = 4.dp
    ) {
        Text(
            text = title,
            style = TextStyle.Default.copy(
                color = AppColor.SunsetOrange,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
            ), modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun GeneralCard(
    width: Dp,
    height: Dp,
    text: String,
    clicked: (() -> Unit)? = null,
    cardPadding: PaddingValues = PaddingValues(0.dp)
) {
    val modifier = if (clicked == null) Modifier
        .width(width)
        .height(height)
    else Modifier
        .width(width)
        .height(height)
        .clickable(onClick = clicked)
    Card(
        modifier = modifier,
        backgroundColor = AppColor.Mustard,
        shape = RoundedCornerShape(16.dp), elevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = cardPadding),
            contentAlignment = Alignment.Center
        ) {
            AutoTextSize(
                text = text,
                textStyle = TextStyle.Default.copy(
                    color = AppColor.SunsetOrange,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                ), maxLines = 10
            )
        }
    }
}

private fun sendDataToMixApi(
    viewModel: SpecialGameScreenViewModel,
    localContext: Context,
    mixpanelAPI: MixpanelAPI,
    template: String
) {
    val events = mapOf(
        Pair(
            viewModel.randomPackage.value?.packageName
                ?: localContext.getString(R.string.special_game_mode_package_name), template
        )
    )
    mixpanelAPI.multiEventSend(
        localContext.getString(R.string.special_game_mode_question_dialog),
        events
    )
}