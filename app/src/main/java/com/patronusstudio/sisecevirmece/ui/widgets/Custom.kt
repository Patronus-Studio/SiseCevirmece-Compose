package com.patronusstudio.sisecevirmece.ui.widgets

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.ui.theme.*

@Preview
@Composable
fun LevelBar(currentStar: Int = 38, nextLevelNeedStar: Int = 40, currentLevel: String = "7") {
    val width = LocalConfiguration.current.screenWidthDp
    val contentWidth = (width * 0.5).dp
    val contentHeight = (width * 0.20).dp
    val barWidth = (width * 0.5).dp
    val barHeight = (width * 0.1).dp
    val circleLevelSize = (width * 0.13).dp
    val maxLevelWidth = barWidth - circleLevelSize
    val starSize = (width * 0.06).dp
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .width(contentWidth)
                .height(contentHeight),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(barWidth)
                    .height(barHeight)
                    .clip(CircleShape)
                    .background(DavysGrey)
                    .border(BorderStroke(2.dp, Mustard), CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        // TODO: 10.dp silinecek
                        .width(maxLevelWidth - 10.dp)
                        .height(barHeight)
                        .clip(RoundedCornerShape(16.dp))
                        .background(UnitedNationsBlue)
                )
                Row(
                    modifier = Modifier
                        .width(maxLevelWidth)
                        .height(barHeight),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$currentLevel/$nextLevelNeedStar",
                        style = TextStyle(color = Color.White)
                    )
                    Image(
                        modifier = Modifier.size(starSize),
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = ""
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Box(
                    modifier = Modifier
                        .size(circleLevelSize + 3.dp)
                        .clip(CircleShape)
                        .border(BorderStroke(2.dp, Mustard), shape = CircleShape)
                        .background(SunsetOrange)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentLevel,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(color = Color.White)
                        )
                        Text(
                            text = stringResource(id = R.string.level),
                            fontSize = 8.sp,
                            style = TextStyle(color = Color.White)
                        )
                    }
                }
            }
        }

    }
}

@Preview
@Composable
private fun UserPic() {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val imageSize = (screenWidth * 0.4).dp

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape)
                .background(Color.White)
                .shadow(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.player_girl), contentDescription = "",
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GreyTranspancy20)
            )
            Canvas(modifier = Modifier
                .fillMaxSize(), onDraw = {
                val rect = Rect(Offset.Zero, size)
                val yellowSize = Size(rect.width + 100f, rect.height - 100f)
                drawLine(
                    Color.Yellow,
                    start = yellowSize.center,
                    end = rect.topRight,
                    strokeWidth = 20f
                )
                val blueSize = Size(rect.width - 100f, rect.height + 100f)
                drawLine(
                    Color.Blue,
                    start = blueSize.center,
                    end = rect.bottomLeft,
                    strokeWidth = 20f
                )
            })
        }
    }

}