package com.patronusstudio.sisecevirmece2.ui.views.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import com.patronusstudio.sisecevirmece2.data.enums.InterstitialAdViewLoadStatusEnum
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.QuestionDbModel
import com.patronusstudio.sisecevirmece2.data.utils.getActivity
import com.patronusstudio.sisecevirmece2.data.utils.multiEventSend
import com.patronusstudio.sisecevirmece2.data.utils.showLog
import com.patronusstudio.sisecevirmece2.data.utils.showSample
import com.patronusstudio.sisecevirmece2.data.viewModels.NormalGameScreenViewModel
import com.patronusstudio.sisecevirmece2.ui.theme.AppColor
import com.patronusstudio.sisecevirmece2.ui.widgets.AutoTextSize
import com.patronusstudio.sisecevirmece2.ui.widgets.InterstitialAdView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun TruthDareQuestionDialog(
    mixpanelAPI: MixpanelAPI,
    closeClicked: () -> Unit,
    viewModel: NormalGameScreenViewModel
) {
    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp
    val smallCardHeight = (height * 0.06).dp
    val smallPaddingHeight = (height * 0.03).dp
    val changeQuestionStatus = remember { mutableStateOf(false) }
    val currentQuestion = remember { mutableStateOf<QuestionDbModel?>(null) }
    val isClickable = remember { mutableStateOf(true) }
    val localContext = LocalContext.current
    LaunchedEffect(key1 = Unit, block = {
        val question = withContext(Dispatchers.IO) {
            viewModel.getRandomQuestion()
        }
        question?.let {
            viewModel.removeQuestionOnList(it)
            withContext(Dispatchers.IO) {
                viewModel.updateQuestionShowStatu(it.primaryId)
            }
            currentQuestion.value = it
        }
    })
    LaunchedEffect(key1 = changeQuestionStatus.value, block = {
        if (isClickable.value) {
            isClickable.value = false
            var question = viewModel.getRandomQuestion()
            if (question == null) {
                withContext(Dispatchers.IO) {
                    viewModel.updateAllQuestionShowStatu()
                }
                withContext(Dispatchers.IO) {
                    viewModel.getTruthDareQuestions()
                }
                question = viewModel.getRandomQuestion()

            } else {
                withContext(Dispatchers.IO) {
                    viewModel.removeQuestionOnList(question)
                }
            }
            currentQuestion.value = question
            withContext(Dispatchers.IO) {
                viewModel.updateQuestionShowStatu(currentQuestion.value!!.primaryId)
            }
            isClickable.value = true
            changeQuestionStatus.value = false
        }
    })

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp))
        TitleCard(
            (width * 0.9).dp,
            viewModel.truthDareSelected.collectAsState().value.getText(localContext)
        )
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GeneralCard(
                    (width * 0.9).dp, (height * 0.2).dp,
                    text = currentQuestion.value?.question ?: "",
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
                            val events = mapOf(
                                Pair(
                                    viewModel.truthDareSelected.value.getText(localContext),
                                    localContext.getString(R.string.replied)
                                )
                            )
                            mixpanelAPI.multiEventSend(
                                localContext.getString(R.string.truth_dare_question_dialog),
                                events
                            )
                            closeClicked()
                        }
                    )
                    GeneralCard(
                        width = (width * 0.45).dp,
                        smallCardHeight,
                        text = stringResource(R.string.change_question),
                        clicked = {
                            val events = mapOf(
                                Pair(
                                    viewModel.truthDareSelected.value.getText(localContext),
                                    localContext.getString(R.string.change_question)
                                )
                            )
                            mixpanelAPI.multiEventSend(
                                localContext.getString(R.string.truth_dare_question_dialog),
                                events
                            )
                            val random = (0..10).random()
                            showLog(random.toString())
                            if (isClickable.value) {
                                if(random < 9){
                                    changeQuestionStatus.value = true
                                }
                                else{
                                    viewModel.setLoadingStatus(true)
                                    InterstitialAdView.loadInterstitial(localContext.getActivity(),
                                        BuildConfig.normal_game_interstitial) { ad ->
                                        when (ad) {
                                            InterstitialAdViewLoadStatusEnum.SHOWED -> {
                                                viewModel.setLoadingStatus(false)
                                            }
                                            InterstitialAdViewLoadStatusEnum.DISSMISSED -> {
                                                viewModel.setLoadingStatus(false)
                                                changeQuestionStatus.value = true
                                            }
                                            else -> {
                                                localContext.showSample()
                                                viewModel.setLoadingStatus(false)
                                            }
                                        }
                                    }
                                }
                            }
                        })
                }
                Spacer(modifier = Modifier.height(smallPaddingHeight))
                GeneralCard(
                    (width * 0.9).dp,
                    smallCardHeight,
                    text = stringResource(R.string.i_wil_ask_question),
                    clicked = {
                        val events = mapOf(
                            Pair(
                                viewModel.truthDareSelected.value.getText(localContext),
                                localContext.getString(R.string.i_wil_ask_question)
                            )
                        )
                        mixpanelAPI.multiEventSend(
                            localContext.getString(R.string.truth_dare_question_dialog),
                            events
                        )
                        closeClicked()
                    }
                )
            }
        }
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