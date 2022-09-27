package com.patronusstudio.sisecevirmece.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

enum class AnimStatu {
    LOADING,
    SUCCES,
    ERROR
}

@Composable
fun LoadingAnimation(dismiss: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
        Dialog(onDismissRequest = { dismiss() }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
            }
        }

    }
}