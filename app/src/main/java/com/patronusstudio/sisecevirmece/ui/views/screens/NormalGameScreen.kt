package com.patronusstudio.sisecevirmece.ui.views.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.enums.BottleTouchListener
import com.patronusstudio.sisecevirmece.data.enums.TruthDareDefaultPackageEnum
import com.patronusstudio.sisecevirmece.data.enums.TruthDareEnum
import com.patronusstudio.sisecevirmece.data.viewModels.NormalGameScreenViewModel
import com.patronusstudio.sisecevirmece.ui.theme.AppColor
import com.patronusstudio.sisecevirmece.ui.views.dialogs.TruthDareQuestionDialog
import com.patronusstudio.sisecevirmece.ui.views.dialogs.TruthDareSelectDialog
import com.patronusstudio.sisecevirmece.ui.widgets.CardTitle
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun NormalGameScreen(backClicked: () -> Unit) {
    val viewModel = hiltViewModel<NormalGameScreenViewModel>()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val bottleSize = (screenWidth * 0.9).dp
    val bottleRotationValue = 10f
    var spinTimer = 0
    val degree = remember { mutableStateOf(0f) }
    val isSpinning = remember { mutableStateOf(false) }
    val animFinished = {
        degree.value = degree.value % 360
        viewModel.setBottleTouchListener(BottleTouchListener.ANIM_ENDED)
        isSpinning.value = false
    }
    val bottleFlipAnim = rememberUpdatedState(newValue = animateFloatAsState(
        targetValue = degree.value,
        animationSpec = tween(durationMillis = 5000), finishedListener = { animFinished() }
    ))
    LaunchedEffect(key1 = Unit, block = {
        viewModel.setTruthDareSelected(TruthDareEnum.TRUTH)
        viewModel.getTruthDareQuestions()
        viewModel.setTruthDareSelected(TruthDareEnum.DARE)
        viewModel.getTruthDareQuestions()
    })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.BlueViolet)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CardTitle(title = stringResource(id = R.string.play_normal_title), backClicked)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.bottle_sample), contentDescription = "",
                modifier = Modifier
                    .size(bottleSize)
                    .rotate(
                        when (viewModel.bottleTouchListener.collectAsState().value) {
                            BottleTouchListener.ANIM_STARTED -> bottleFlipAnim.value.value
                            else -> degree.value
                        }
                    )
                    .pointerInput(Unit) {
                        this.detectTransformGestures { centroid, pan, zoom, rotation ->
                            if (isSpinning.value.not()) {
                                if (pan.x > 0 && pan.y > 0) degree.value += bottleRotationValue
                                else if (pan.x < 0 && pan.y < 0) degree.value -= bottleRotationValue
                                else if (pan.x > 0 && pan.y < 0) degree.value -= bottleRotationValue
                                else degree.value += bottleRotationValue
                                spinTimer++
                            }
                        }
                    }
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            awaitFirstDown(requireUnconsumed = false)
                            do {
                                val event = awaitPointerEvent()
                                val canceled =
                                    event.changes.any { it.isConsumed && it.positionChanged() }
                            } while (!canceled && event.changes.any { it.pressed })
                            if (isSpinning.value.not()) {
                                viewModel.setBottleTouchListener(BottleTouchListener.ANIM_STARTED)
                                val result = degree.value % 100
                                degree.value = if (result < 50)
                                    (result * (Random.nextInt(50, 75)) + spinTimer)
                                else (result * (Random.nextInt(20, 35)) + spinTimer)
                                spinTimer = 0
                                isSpinning.value = true
                            }
                        }
                    }
                    .combinedClickable(onDoubleClick = {
                        viewModel.setBottleTouchListener(BottleTouchListener.ANIM_ENDED)
                    }, onClick = {

                    })
            )
        }
    }
    if (viewModel.bottleTouchListener.collectAsState().value == BottleTouchListener.ANIM_ENDED) {
        TruthDareSelectDialog(dissmissed = {
            viewModel.setBottleTouchListener(BottleTouchListener.INIT)
        }, truthDareSelected = {
            viewModel.setTruthDareSelected(it)
            viewModel.setBottleTouchListener(BottleTouchListener.TRUTH_DARE_SELECTED)
        })
    }
    if (viewModel.bottleTouchListener.collectAsState().value == BottleTouchListener.TRUTH_DARE_SELECTED) {
        Dialog(
            onDismissRequest = {
                viewModel.setBottleTouchListener(BottleTouchListener.INIT)
            }, properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            if (viewModel.truthDareSelected.collectAsState().value == TruthDareEnum.NOT_SELECTED) {
                viewModel.setBottleTouchListener(BottleTouchListener.INIT)
            } else {
                TruthDareQuestionDialog(
                    closeClicked = { viewModel.setBottleTouchListener(BottleTouchListener.INIT) },
                    viewModel
                )
            }
        }
    }
}

