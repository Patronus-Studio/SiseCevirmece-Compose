package com.patronusstudio.sisecevirmece.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardImageWithText(
    @DrawableRes image: Int,
    text: String,
    backgroundColor: Color,
    textColor: Color,
    borderColor: Color? = null,
    cardSizeWidth: Dp,
    cardSizeHeight: Dp,
    imageSize: Dp,
    clicked: () -> Unit,
) {
    Card(
        modifier = Modifier
            .width(cardSizeWidth)
            .height(cardSizeHeight)
            .clickable { clicked() },
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(16.dp),
        border = if (borderColor != null) BorderStroke(1.dp, borderColor)
        else BorderStroke(0.dp, Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = image), contentDescription = "",
                modifier = Modifier.size(imageSize)
            )
            Text(text = text, fontSize = 12.sp, style = TextStyle(color = textColor))
        }
    }
}