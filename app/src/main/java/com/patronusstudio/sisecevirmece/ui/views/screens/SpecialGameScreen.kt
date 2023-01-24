package com.patronusstudio.sisecevirmece.ui.views.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.enums.BottleTouchListener
import com.patronusstudio.sisecevirmece.data.viewModels.SpecialGameScreenViewModel
import com.patronusstudio.sisecevirmece.ui.views.dialogs.SpecialQuestionDialog
import com.patronusstudio.sisecevirmece.ui.widgets.BaseBackground
import kotlin.random.Random

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SpecialGameScreen(selectedPackages: String, backClicked: () -> Unit) {
    val viewModel = hiltViewModel<SpecialGameScreenViewModel>()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val bottleSize = (screenWidth * 0.9).dp
    val bottleRotationValue = 10f
    var spinTimer = 0
    val degree = remember { mutableStateOf(0f) }
    val isSpinning = remember { mutableStateOf(false) }
    val animFinished = {
        viewModel.setBottleTouchListener(BottleTouchListener.ANIM_ENDED)
        isSpinning.value = false
    }
    val bottleFlipAnim = animateFloatAsState(
        targetValue = degree.value,
        animationSpec = tween(durationMillis = 5000), finishedListener = {
            animFinished()
        }
    )
    LaunchedEffect(key1 = Unit, block = {
        viewModel.jsonToModel(selectedPackages)
    })

    BaseBackground(titleId = R.string.play_special_title, backClicked = backClicked) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.bottle_sample), contentDescription = "",
                modifier = Modifier
                    .size(bottleSize)
                    .rotate(
                        when (viewModel.bottleTouchListener.collectAsState().value) {
                            BottleTouchListener.ANIM_STARTED -> {
                                bottleFlipAnim.value
                            }
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
            )
        }
    }
    if (viewModel.bottleTouchListener.collectAsState().value == BottleTouchListener.ANIM_ENDED) {
        Dialog(
            onDismissRequest = {
                viewModel.setBottleTouchListener(BottleTouchListener.INIT)
            }, properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            SpecialQuestionDialog(closeClicked = {
                viewModel.setBottleTouchListener(BottleTouchListener.INIT)
            }, viewModel = viewModel)
        }

    }
}
