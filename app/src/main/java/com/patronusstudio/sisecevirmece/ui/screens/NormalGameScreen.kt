package com.patronusstudio.sisecevirmece.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.ui.theme.AppColor
import com.patronusstudio.sisecevirmece.ui.widgets.CardTitle

@Composable
fun NormalGameScreen(backClicked: () -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val bottleSize = (screenWidth * 0.9).dp
    val degree = remember {
        mutableStateOf(0f)
    }
    val bottleRotationValue = 10f
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.BlueViolet)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CardTitle(title = "Normal MOD", backClicked)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.bottle_sample),
                contentDescription = "",
                modifier = Modifier
                    .size(bottleSize)
                    .rotate(degree.value)
                    .pointerInput(Unit) {
                        this.detectTransformGestures { centroid, pan, zoom, rotation ->
                            if (pan.x > 0 && pan.y > 0) degree.value += bottleRotationValue
                            else if (pan.x < 0 && pan.y < 0) degree.value -= bottleRotationValue
                            else if (pan.x > 0 && pan.y < 0) degree.value -= bottleRotationValue
                            else degree.value += bottleRotationValue
                        }
                    }
            )

        }
    }
}

@Composable
private fun RotatebleBottle(
    bottleSize: Dp,
    degree: Float,
    bottleRotationValue: Float,
    changedDegree: (Float) -> Unit
) {

}