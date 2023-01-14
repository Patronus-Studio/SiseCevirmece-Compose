package com.patronusstudio.sisecevirmece.ui.views.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

val passeblaDragAmont = -50
var isPassedOtherScreen = false

suspend fun timer() {
    isPassedOtherScreen = true
    delay(250)
    isPassedOtherScreen = false
}

@Composable
fun LandingFirstScreen(pass: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(Random.nextLong(0xFFFFFFFF)))
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    if (isPassedOtherScreen.not() && dragAmount > passeblaDragAmont) {
                        CoroutineScope(Dispatchers.Main).launch {
                            timer()
                            pass()
                        }
                    }
                }
            }, contentAlignment = Alignment.Center
    ) {
        Text(text = "First Screen")
    }
}

@Composable
fun LandingSecondScreen(pass: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(Random.nextLong(0xFFFFFFFF)))
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    if (isPassedOtherScreen.not() && dragAmount > passeblaDragAmont) {
                        CoroutineScope(Dispatchers.Main).launch {
                            timer()
                            pass()
                        }
                    }
                }
            }, contentAlignment = Alignment.Center
    ) {
        Text(text = "Second Screen")
    }
}

@Composable
fun LandingThirdScreen(pass: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(Random.nextLong(0xFFFFFFFF)))
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    if (isPassedOtherScreen.not() && dragAmount > passeblaDragAmont) {
                        CoroutineScope(Dispatchers.Main).launch {
                            timer()
                            pass()
                        }
                    }
                }
            }, contentAlignment = Alignment.Center
    ) {
        Text(text = "Third Screen")
    }
}

@Composable
fun LandingLastScreen(pass: () -> Unit) {
    val mContext = LocalContext.current.applicationContext
    val isVisible = remember { mutableStateOf(false) }
    val randomColor = remember { mutableStateOf(Color(Random.nextLong(0xFFFFFFFF))) }

    LaunchedEffect(key1 = Unit, block = {
        launch {
            delay(500)
            isVisible.value = true
        }
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(randomColor.value),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = isVisible.value) {
            FloatingActionButton(onClick = {
                pass()
            }) {

            }
        }
    }
}
