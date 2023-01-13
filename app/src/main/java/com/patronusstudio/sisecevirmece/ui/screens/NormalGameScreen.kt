package com.patronusstudio.sisecevirmece.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.enums.TruthDareEnum
import com.patronusstudio.sisecevirmece.ui.theme.AppColor
import com.patronusstudio.sisecevirmece.ui.widgets.CardTitle
import kotlinx.coroutines.delay
import kotlin.random.Random

private enum class TouchListener {
    INIT,
    ANIM_STARTED,
    ANIM_ENDED
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NormalGameScreen(backClicked: () -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val bottleSize = (screenWidth * 0.9).dp
    val degree = remember { mutableStateOf(0f) }
    val bottleRotationValue = 10f
    var spinTimer = 0
    val touchStatus = remember { mutableStateOf(TouchListener.INIT) }
    val isSpinning = remember { mutableStateOf(false) }
    val blurSize = remember {
        mutableStateOf(0.dp)
    }
    val animFinished = {
        degree.value = degree.value % 360
        touchStatus.value = TouchListener.ANIM_ENDED
        isSpinning.value = false
        blurSize.value = 8.dp
    }
    val infiniteAnim = rememberUpdatedState(newValue = animateFloatAsState(
        targetValue = degree.value,
        animationSpec = tween(durationMillis = 5000), finishedListener = { animFinished() }
    ))


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.BlueViolet)
            .blur(blurSize.value)
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
                                touchStatus.value = TouchListener.ANIM_STARTED
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
                        blurSize.value = 8.dp
                        touchStatus.value = TouchListener.ANIM_ENDED
                    }, onClick = {

                    })
            )
        }
    }
    if (touchStatus.value == TouchListener.ANIM_ENDED) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    blurSize.value = 0.dp
                    touchStatus.value = TouchListener.INIT
                }, verticalArrangement = Arrangement.SpaceEvenly
        ) {
            TruthDareSelectPopup()
        }
    }
}

@Composable
private fun TruthDareSelectPopup() {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val context = LocalContext.current
    val cardWidth = (screenWidth * 0.44).dp
    val cardHeight = (screenHeight * 0.3).dp

    val isVisible = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit, block = {
        delay(250L)
        isVisible.value = true
    })
    val clicked = { truthDareEnum: TruthDareEnum ->
        Toast.makeText(context, truthDareEnum.getText(context), Toast.LENGTH_SHORT).show()
    }


    AnimatedCard(isVisible.value, cardWidth, cardHeight, true, TruthDareEnum.TRUTH, clicked)
    AnimatedCard(isVisible.value, cardWidth, cardHeight, false, TruthDareEnum.DARE, clicked)
}

@Composable
private fun AnimatedCard(
    isVisible: Boolean,
    cardWidth: Dp,
    cardHeight: Dp,
    isLeftToRight: Boolean,
    truthDareEnum: TruthDareEnum,
    clicked: (TruthDareEnum) -> Unit,
    animDurationMillis: Int = 1250,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            initialOffsetX = { if (isLeftToRight.not()) -it else it },
            animationSpec = tween(
                durationMillis = animDurationMillis
            )
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = animDurationMillis, 0
            )
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            TDCard(cardWidth = cardWidth, cardHeight = cardHeight, truthDareEnum, clicked)
        }
    }

}

@Composable
private fun TDCard(
    cardWidth: Dp, cardHeight: Dp, truthDareEnum: TruthDareEnum,
    clicked: (TruthDareEnum) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .width(cardWidth)
                .height(cardHeight)
                .background(AppColor.Mustard, RoundedCornerShape(16.dp))
                .clickable { clicked(truthDareEnum) }
        ) {
            Column(
                Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(id = truthDareEnum.getImageId()),
                    contentDescription = null
                )
                Text(
                    text = truthDareEnum.getText(LocalContext.current),
                    style = TextStyle.Default.copy(
                        color = AppColor.SunsetOrange,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}