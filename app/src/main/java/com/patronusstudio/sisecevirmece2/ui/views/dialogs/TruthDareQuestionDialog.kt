package com.patronusstudio.sisecevirmece2.ui.views.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.patronusstudio.sisecevirmece2.BuildConfig
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.QuestionDbModel
import com.patronusstudio.sisecevirmece2.data.utils.BetmRounded
import com.patronusstudio.sisecevirmece2.data.utils.showLog
import com.patronusstudio.sisecevirmece2.data.viewModels.NormalGameScreenViewModel
import com.patronusstudio.sisecevirmece2.ui.theme.AppColor
import com.patronusstudio.sisecevirmece2.ui.widgets.AdmobInterstialAd
import com.patronusstudio.sisecevirmece2.ui.widgets.AutoTextSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun TruthDareQuestionDialog(
    closeClicked: () -> Unit,
    viewModel: NormalGameScreenViewModel
) {
    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp
    val smallCardHeight = (height * 0.06).dp
    val smallPaddingHeight = (height * 0.03).dp
    val currentQuestion = remember { mutableStateOf<QuestionDbModel?>(null) }
    val questionChangeClicked = remember { mutableStateOf(false) }
    val questionChangeListener = remember { mutableStateOf(false) }
    val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)
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
    LaunchedEffect(key1 = questionChangeListener.value, block = {
        if (questionChangeListener.value) {
            questionChangeListener.value = false
            viewModel.setLoadingStatus(true)
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
            viewModel.setLoadingStatus(false)
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
                            questionChangeClicked.value = true
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
                        closeClicked()
                    }
                )
            }
        }
    }
    if (questionChangeClicked.value) {
        questionChangeClicked.value = false
        val random = (0..10).random()
        showLog(random.toString())
        if (random < 1) {
            questionChangeListener.value = true
        } else {
            viewModel.setLoadingStatus(true)
            AdmobInterstialAd(
                context = localContext,
                addUnitId = BuildConfig.normal_game_interstitial,
                failedLoad = {
                    questionChangeListener.value = true
                    viewModel.setLoadingStatus(false)
                }, adClosed = {
                    questionChangeListener.value = true
                    viewModel.setLoadingStatus(false)
                }
            )
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
                fontFamily = BetmRounded,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
            ),
            modifier = Modifier
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
                    textAlign = TextAlign.Center,
                    fontFamily = BetmRounded
                ), maxLines = 10
            )
        }
    }
}