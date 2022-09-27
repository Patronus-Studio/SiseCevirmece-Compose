package com.patronusstudio.sisecevirmece.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import com.patronusstudio.sisecevirmece.data.enums.LandingScreenEnum
import com.patronusstudio.sisecevirmece.data.model.LandingModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun LandingScreen() {

    val list = mutableListOf(
        LandingModel("INIT_PAGE", LandingScreenEnum.INIT_PAGE),
        LandingModel("Hoşgeldiniz", LandingScreenEnum.FIRST_PAGE),
        LandingModel("Nerede kalmıştık?", LandingScreenEnum.SECOND_PAGE),
        LandingModel("2022 bitti", LandingScreenEnum.THIRD_PAGE),
    )

    Landing(list) { model ->
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "${model.text}\n${model.landingScreenEnum.pageCount()}")
        }
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Landing(list: List<LandingModel>, content: @Composable (LandingModel) -> Unit) {
    val currentItem = remember { mutableStateOf(list[0]) }
    val isDraggable = remember { mutableStateOf(true) }
    val isAnimated = remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(currentItem.value.color))
            .pointerInput(Unit) {
                detectHorizontalDragGestures(onHorizontalDrag = { change: PointerInputChange, dragAmount: Float ->
                    if (isDraggable.value) {
                        if (dragAmount < -50) {
                            if (currentItem.value.landingScreenEnum.pageCount() < list.last().landingScreenEnum.pageCount() ) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    isAnimated.value = false
                                    isDraggable.value = false
                                    currentItem.value = list[currentItem.value.landingScreenEnum.pageCount()+1]
                                    delay(200L)
                                    isAnimated.value = true
                                    delay(200L)
                                    isDraggable.value = true
                                }
                            }
                        }
                        if (dragAmount > 50) {
                            if (currentItem.value.landingScreenEnum.pageCount() > list.first().landingScreenEnum.pageCount()) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    isAnimated.value = false
                                    isDraggable.value = false
                                    currentItem.value = list[currentItem.value.landingScreenEnum.pageCount() - 1]
                                    delay(200L)
                                    isAnimated.value = true
                                    delay(200L)
                                    isDraggable.value = true
                                }
                            }
                        }
                    }
                })
            }
    ) {
        //https://proandroiddev.com/animate-with-jetpack-compose-animate-as-state-and-animation-specs-ffc708bb45f8
        AnimatedContent(targetState = currentItem.value,
            transitionSpec = {
                if (this.targetState.landingScreenEnum.pageCount() > initialState.landingScreenEnum.pageCount()) {
                    slideInHorizontally(tween(500)) { width -> width } + fadeIn(tween(500)) with
                            slideOutHorizontally { width -> -width } + fadeOut(tween(500))
                } else {
                    slideInHorizontally(tween(500)) { width -> -width } + fadeIn(tween(500)) with
                            slideOutHorizontally { width -> width } + fadeOut(tween(500))
                }.using(
                    SizeTransform(clip = false)
                )
            }, content = {
                content(currentItem.value)
            })
    }
}
