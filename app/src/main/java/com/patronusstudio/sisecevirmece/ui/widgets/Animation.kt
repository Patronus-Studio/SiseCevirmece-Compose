package com.patronusstudio.sisecevirmece.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.patronusstudio.sisecevirmece.R


@Composable
fun LoadingAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bottle_anim))
    val progress by animateLottieCompositionAsState(composition)
    Dialog(onDismissRequest = {}) {
        Surface(
            modifier = Modifier.size(120.dp),
            elevation = 4.dp,
            shape = RoundedCornerShape(100)
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}