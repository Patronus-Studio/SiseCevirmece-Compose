package com.patronusstudio.sisecevirmece2.ui.views.screens

import android.media.MediaPlayer
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.patronusstudio.sisecevirmece2.BuildConfig
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.data.enums.BottleTouchListener
import com.patronusstudio.sisecevirmece2.data.enums.TruthDareEnum
import com.patronusstudio.sisecevirmece2.data.viewModels.NormalGameScreenViewModel
import com.patronusstudio.sisecevirmece2.ui.screens.LoadingAnimation
import com.patronusstudio.sisecevirmece2.ui.views.dialogs.TruthDareQuestionDialog
import com.patronusstudio.sisecevirmece2.ui.views.dialogs.TruthDareSelectDialog
import com.patronusstudio.sisecevirmece2.ui.widgets.BannerAdView
import com.patronusstudio.sisecevirmece2.ui.widgets.BaseBackground
import kotlin.random.Random

@Composable
fun NormalGameScreen(backClicked: () -> Unit) {
    val bottleSoundPlayer = MediaPlayer.create(LocalContext.current, R.raw.bottle_sound_1)
    val viewModel = hiltViewModel<NormalGameScreenViewModel>()
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
        }, label = ""
    )
    BackHandler {
        bottleSoundPlayer.stop()
        backClicked()
    }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getActiveBackground()
        viewModel.getBottleOnDb()
        viewModel.setTruthDareSelected(TruthDareEnum.TRUTH)
        viewModel.getTruthDareQuestions()
        viewModel.setTruthDareSelected(TruthDareEnum.DARE)
        viewModel.getTruthDareQuestions()
        isSpinning.value = false
    })
    LaunchedEffect(key1 = viewModel.bottleTouchListener.collectAsState().value, block = {
        if (viewModel.bottleTouchListener.value == BottleTouchListener.ANIM_STARTED) {
            bottleSoundPlayer.seekTo(0)
            bottleSoundPlayer.start()
        } else if (viewModel.bottleTouchListener.value == BottleTouchListener.ANIM_ENDED) {
            bottleSoundPlayer.pause()
        }
    })
    BaseBackground(
        titleId = R.string.play_normal_title,
        backClicked = {
            bottleSoundPlayer.stop()
            backClicked()
        },
        contentOnFullScreen = {
            AsyncImage(
                model = viewModel.backgroundModel.collectAsState().value?.packageImage
                    ?: R.drawable.background_original, contentDescription = "",
                contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
            )
            AnimatedVisibility(
                visible = viewModel.bottleTouchListener.collectAsState().value == BottleTouchListener.INIT ||
                        viewModel.bottleTouchListener.collectAsState().value == BottleTouchListener.ANIM_STARTED ||
                        viewModel.bottleTouchListener.collectAsState().value == BottleTouchListener.ANIM_STARTED,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                BannerAdView(BuildConfig.in_game_normal_banner, Alignment.BottomCenter)
            }
        },
        contentOnTitleBottom = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = viewModel.activeBottle.collectAsState().value?.packageImage
                        ?: R.drawable.bottle_sample,
                    contentDescription = "",
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
                                if (isSpinning.value.not() && degree.value != 0f) {
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
                        viewModel.setLoadingStatus(false)
                    }, properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    TruthDareQuestionDialog(
                        closeClicked = {
                            viewModel.setBottleTouchListener(BottleTouchListener.INIT)
                            viewModel.setLoadingStatus(false)
                        },
                        viewModel = viewModel
                    )
                }
            }
            AnimatedVisibility(visible = viewModel.isLoading.value) {
                LoadingAnimation()
            }
        })
}

