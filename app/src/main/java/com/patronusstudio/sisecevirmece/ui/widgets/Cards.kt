package com.patronusstudio.sisecevirmece.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.ui.theme.DavysGrey
import com.patronusstudio.sisecevirmece.ui.theme.Mustard
import com.patronusstudio.sisecevirmece.ui.theme.SunsetOrange
import com.patronusstudio.sisecevirmece.ui.theme.UnitedNationsBlue

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
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable { clicked() })
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