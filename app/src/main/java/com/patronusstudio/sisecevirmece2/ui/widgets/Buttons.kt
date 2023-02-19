package com.patronusstudio.sisecevirmece2.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.data.utils.BetmRounded


@Composable
fun ButtonWithDot(
    height: Int,
    dotColor: Color,
    btnColor: Color,
    text: String,
    textColor: Color,
    clicked: () -> Unit
) {
    val cornerShape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .wrapContentWidth()
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
            }
            .clickable {
                clicked()
            }
            .padding(horizontal = (height / 4).dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .height((height - height * 0.1).dp)
                .background(btnColor, cornerShape)
                .clip(cornerShape)
                .padding(horizontal = (height / 4).dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontFamily = BetmRounded,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun ButtonWithPassive(
    height: Int,
    btnColor: Color,
    text: String,
    textColor: Color,
    clicked: () -> Unit
) {
    val cornerShape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .height(height.dp)
            .padding(horizontal = (height / 4).dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .height((height - height * 0.1).dp)
                .background(btnColor, cornerShape)
                .clip(cornerShape)
                .clickable {
                    clicked()
                }
                .padding(horizontal = (height / 4).dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontFamily = BetmRounded,
                fontWeight = FontWeight.Normal
            )
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