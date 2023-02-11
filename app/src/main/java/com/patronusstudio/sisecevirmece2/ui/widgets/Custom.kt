package com.patronusstudio.sisecevirmece2.ui.widgets

import android.content.res.Resources
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.data.enums.AnimMillis
import com.patronusstudio.sisecevirmece2.data.model.AvatarModel
import com.patronusstudio.sisecevirmece2.ui.theme.AppColor

val Float.toDp get() = this / Resources.getSystem().displayMetrics.density

@Preview
@Composable
fun LevelBar(currentStar: Int = 35, nextLevelNeedStar: Int = 40, currentLevel: String = "7") {
    val width = LocalConfiguration.current.screenWidthDp
    val contentWidth = (width * 0.5).dp
    val contentHeight = (width * 0.20).dp
    val barWidth = (width * 0.5).dp
    val barHeight = (width * 0.1).dp
    val circleLevelSize = (width * 0.13).dp
    val maxLevelWidth = barWidth + 16.dp - circleLevelSize - 4.dp

    val currentLeverBarSize: Dp =
        (((currentStar * 100 / nextLevelNeedStar) * maxLevelWidth.value) / 100).dp
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
                    .background(AppColor.DavysGrey)
                    .border(BorderStroke(2.dp, AppColor.Mustard), CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .width(currentLeverBarSize)
                        .height(barHeight)
                        .clip(RoundedCornerShape(16.dp))
                        .background(AppColor.UnitedNationsBlue)
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
                        .border(BorderStroke(2.dp, AppColor.Mustard), shape = CircleShape)
                        .background(AppColor.SunsetOrange)
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UserPic(
    ratio: Double = 0.25,
    avatar: AvatarModel,
    clickedImage: ((AvatarModel?) -> Unit)? = null
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val imageSize = (screenWidth * ratio).dp
    val image = remember { mutableStateOf<Any?>(null) }

    val imageLoader = ImageLoader(LocalContext.current)
    val imageRequest = ImageRequest.Builder(LocalContext.current).data(avatar.imageUrl)
        .listener(
            onSuccess = { req, suc ->
                image.value = req.data
            }).target {
            image.value = it
        }.build()
    imageLoader.enqueue(imageRequest)

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        AnimatedVisibility(
            visible = image.value != null,
            enter = scaleIn(
                transformOrigin = TransformOrigin(0.5f, 0.5f),
                animationSpec = tween(AnimMillis.NORMAL.millis)
            ) + fadeIn() + expandIn(expandFrom = Alignment.Center)
        ) {
            AsyncImage(
                model = image.value, contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(imageSize)
                    .clip(CircleShape)
                    .clickable {
                        clickedImage!!(avatar)
                    }
            )
        }
        if(image.value == null){
            CircularProgressIndicator()
        }
//                if (avatar.buyedStatu == AvatarStatu.NON_BUYED) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(AppColor.GreyTranspancy20)
//                    )
//                    Canvas(modifier = Modifier
//                        .fillMaxSize(), onDraw = {
//                        val rect = Rect(Offset.Zero, size)
//                        val yellowSize = Size(rect.width + 70f, rect.width - 70f)
//                        drawLine(
//                            Color.White,
//                            start = yellowSize.center,
//                            end = rect.topRight,
//                            strokeWidth = 20f, cap = StrokeCap.Square
//                        )
//                        val blueSize = Size(rect.width - 70f, rect.width + 70f)
//                        drawLine(
//                            Color.White,
//                            start = blueSize.center,
//                            end = rect.bottomLeft,
//                            strokeWidth = 20f, cap = StrokeCap.Square
//                        )
//                        drawOval(
//                            color = Color.White,
//                            topLeft = Offset(rect.center.x - 40f, rect.center.y - 40f),
//                            size = Size(80f, 80f),
//                            style = Stroke(width = 5f)
//                        )
//                        drawImage(
//                            bitmapImage,
//                            dstOffset = IntOffset(
//                                rect.center.x.toInt() - 25,
//                                rect.center.y.toInt() - 25
//                            ),
//                            dstSize = IntSize(50, 50),
//                        )
//                    })
//                }
    }
}