package com.patronusstudio.sisecevirmece.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun LandingFirstScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Green)
        .pointerInput(Unit,{
            detectHorizontalDragGestures { change, dragAmount ->
                change
                dragAmount
            }
        })) {

    }
}
