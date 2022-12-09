package com.patronusstudio.sisecevirmece.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronusstudio.sisecevirmece.R

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
            .clickable {
                clicked()
            },
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

@Composable
fun CardTitle(title: String, clickedBackButton: () -> Unit) {
    val cardWidth = (LocalConfiguration.current.screenWidthDp * 0.9)
    val screenHeight = LocalConfiguration.current.screenHeightDp
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeightIn(min = (screenHeight * 0.08).dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .width(cardWidth.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Player Man",
                        modifier = Modifier
                            .width((cardWidth * 0.1).dp)
                            .clip(CircleShape)
                            .clickable {
                                clickedBackButton()
                            }
                    )
                    Spacer(modifier = Modifier.width((cardWidth * 0.05).dp))

                    Text(
                        text = title,
                        modifier = Modifier.width((cardWidth * 0.7).dp),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.SansSerif
                    )
                    Spacer(modifier = Modifier.width((cardWidth * 0.15).dp))
                }
            }
        }
    }
}