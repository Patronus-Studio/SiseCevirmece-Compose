package com.patronusstudio.sisecevirmece.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patronusstudio.sisecevirmece.ui.theme.AppColor
import com.patronusstudio.sisecevirmece.ui.widgets.CardTitle

@Composable
fun GameTypeSelection(back: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.BlueViolet)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CardTitle(title = "Oyun Tipini Seçin") {
            back()
        }
    }
}

@Preview
@Composable
private fun CardButton() {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = AppColor.Mustard,
        modifier = Modifier
            .width(300.dp)
            .height(200.dp)
    ) {

    }
}