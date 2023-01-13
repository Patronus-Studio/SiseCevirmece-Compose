package com.patronusstudio.sisecevirmece.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.ui.theme.AppColor
import com.patronusstudio.sisecevirmece.ui.widgets.CardTitle
import kotlin.random.Random

@Composable
fun NormalGameScreen(backClicked: () -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val bottleSize = (screenWidth * 0.9).dp
    val degree = remember {
        mutableStateOf(0f)
    }
    val bottleRotationValue = 10f
    var spinTimer = 0
    val touchStatus = remember {
        mutableStateOf(TouchListener.INIT)
    }
    val animFinished = {
        degree.value = degree.value % 360
        touchStatus.value = TouchListener.ANIM_ENDED
    }
    val infiniteAnim = rememberUpdatedState(newValue = animateFloatAsState(
        targetValue = degree.value,
        animationSpec = tween(durationMillis = 5000), finishedListener = { animFinished() }
    ))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.BlueViolet)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CardTitle(title = "Normal MOD", backClicked)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.bottle_sample), contentDescription = "",
                modifier = Modifier
                    .size(bottleSize)
                    .rotate(
                        when (touchStatus.value) {
                            TouchListener.ANIM_STARTED -> infiniteAnim.value.value
                            else -> degree.value
                        }
                    )
                    .pointerInput(Unit) {
                        this.detectTransformGestures { centroid, pan, zoom, rotation ->
                            if (pan.x > 0 && pan.y > 0) degree.value += bottleRotationValue
                            else if (pan.x < 0 && pan.y < 0) degree.value -= bottleRotationValue
                            else if (pan.x > 0 && pan.y < 0) degree.value -= bottleRotationValue
                            else degree.value += bottleRotationValue
                            spinTimer++
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
                            touchStatus.value = TouchListener.ANIM_STARTED
                            val result = degree.value % 100
                            degree.value = if (result < 50)
                                (result * (Random.nextInt(50, 75)) + spinTimer)
                            else (result * (Random.nextInt(20, 35)) + spinTimer)
                        }
                    }
            )
        }
    }
}

private enum class TouchListener {
    INIT,
    ANIM_STARTED,
    ANIM_ENDED
}