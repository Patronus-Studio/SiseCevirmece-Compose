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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.ContentScale
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

@Composable
fun ButtonWithDot(
    width: Int,
    height: Int,
    dotColor: Color,
    btnColor: Color,
    text: String,
    textColor: Color
) {
    val cornerShape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
            .background(Color.Transparent)
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .drawWithCache {
                val path = Path()
                onDrawWithContent {
                    clipPath(path) {
                        this@onDrawWithContent.drawContent()
                    }
                    drawContent()
                    val circleSize = size.height / 4f
                    val xLocation = size.width - circleSize
                    val yLocation = circleSize
                    drawCircle(
                        Color.Transparent,
                        radius = circleSize,
                        center = Offset(xLocation, yLocation),
                        blendMode = BlendMode.Clear
                    )
                    drawCircle(
                        dotColor, radius = circleSize * 0.7f,
                        center = Offset(xLocation, yLocation)
                    )
                }
            }, contentAlignment = Alignment.BottomCenter
    ) {

        Box(
            modifier = Modifier
                .width((width - height * 0.1).dp)
                .height((height - height * 0.1).dp)
                .background(btnColor, cornerShape)
                .clip(cornerShape),

            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = textColor)
        }
    }
}


@Composable
fun GraphicsLayerCompositingStrategyExample() {
    Image(painter = painterResource(id = R.drawable.player_girl),
        contentDescription = "Dog",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(120.dp)
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFFC5E1A5),
                        Color(0xFF80DEEA)
                    )
                )
            )
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .drawWithCache {
                val path = Path()
                path.addOval(
                    Rect(
                        topLeft = Offset.Zero,
                        bottomRight = Offset(size.width, size.height)
                    )
                )
                onDrawWithContent {
                    clipPath(path) {
                        this@onDrawWithContent.drawContent()
                    }
                    val dotSize = size.width / 8f
                    drawCircle(
                        Color.Black,
                        radius = dotSize,
                        center = Offset(
                            x = size.width - dotSize,
                            y = size.height - dotSize
                        ),
                        blendMode = BlendMode.Clear
                    )
                    drawCircle(
                        Color(0xFFEF5350), radius = dotSize * 0.8f,
                        center = Offset(
                            x = size.width - dotSize,
                            y = size.height - dotSize
                        )
                    )
                }

            }
    )
}






