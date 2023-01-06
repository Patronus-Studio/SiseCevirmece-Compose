package com.patronusstudio.sisecevirmece.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patronusstudio.sisecevirmece.ui.theme.BlueViolet
import com.patronusstudio.sisecevirmece.ui.widgets.CardTitle

@Composable
fun GameTypeSelection(back: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueViolet)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CardTitle(title = "Oyun Tipini Seçin") {
            back()
        }
    }
}