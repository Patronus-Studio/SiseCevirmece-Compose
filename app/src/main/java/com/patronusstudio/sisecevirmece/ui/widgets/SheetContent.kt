package com.patronusstudio.sisecevirmece.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.patronusstudio.sisecevirmece.R

@Composable
fun ErrorSheet(message: String, errorIconClicked: (() -> Unit)? = null) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable {
                errorIconClicked?.let {
                    errorIconClicked()
                }
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, end = 12.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Image(painter = painterResource(id = R.drawable.error),
                contentDescription = "Close btn",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        errorIconClicked?.let {
                            errorIconClicked()
                        }
                    })
        }
        Column(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(150.dp),
            )
            Text(text = message)
        }
    }
}