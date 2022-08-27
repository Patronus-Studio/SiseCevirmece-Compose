package com.patronusstudio.sisecevirmece.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.ui.theme.*
import com.patronusstudio.sisecevirmece.ui.widgets.CardImageWithText
import com.patronusstudio.sisecevirmece.ui.widgets.LevelBar

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueViolet)
    ) {
        LevelBar()
        HomeCards()
        PlayButton()
    }

}

@Composable
private fun HomeCards() {
    val width = LocalConfiguration.current.screenWidthDp
    val cardSizeWidth = (width * 0.25).dp
    val cardSizeHeight = (width * 0.25).dp
    val imageSize = (width * 0.15).dp
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CardImageWithText(
            R.drawable.category, stringResource(id = R.string.add_category),
            backgroundColor = Color.White, textColor = Heliotrope,
            borderColor = SeaSerpent, cardSizeWidth, cardSizeHeight,
            imageSize
        ) {
            Toast.makeText(context, "Kategorini Ekle", Toast.LENGTH_SHORT)
                .show()
        }
        CardImageWithText(
            R.drawable.store, stringResource(id = R.string.store), backgroundColor = Mustard,
            imageSize = imageSize, textColor = SunsetOrange, cardSizeWidth = cardSizeWidth,
            cardSizeHeight = cardSizeHeight
        ) {
            Toast.makeText(context, "Mağaza", Toast.LENGTH_SHORT).show()
        }
        CardImageWithText(
            R.drawable.profile, stringResource(id = R.string.my_profile),
            backgroundColor = Color.White, textColor = SunsetOrange, borderColor = SeaSerpent,
            cardSizeWidth, cardSizeHeight, imageSize
        ) {
            Toast.makeText(context, "Profilim", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun PlayButton() {
    val width = LocalConfiguration.current.screenWidthDp
    val context = LocalContext.current
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Card(
            modifier = Modifier
                .width(width = (width * 0.8).dp)
                .padding(vertical = 16.dp)
                .clickable {
                    Toast
                        .makeText(context, "Oyna", Toast.LENGTH_SHORT)
                        .show()
                },
            backgroundColor = Mustard,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.play), fontSize = 64.sp,
                style = TextStyle(color = SunsetOrange), textAlign = TextAlign.Center
            )
        }
    }
}