package com.patronusstudio.sisecevirmece2.ui.widgets

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.data.enums.PackageDetailCardBtnEnum
import com.patronusstudio.sisecevirmece2.data.model.PackageModel
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.BackgroundDbModel
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece2.ui.theme.AppColor

@Composable
fun CardImageWithText(
    image: Any,
    text: String,
    backgroundColor: Color,
    textColor: Color,
    borderColor: Color? = null,
    cardSizeWidth: Dp,
    cardSizeHeight: Dp,
    imageSize: Dp,
    clicked: () -> Unit,
) {
    Log.d("call", text)
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
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier
                    .size(imageSize)
                    .clip(RoundedCornerShape(4.dp))
            )
            Text(
                text = text,
                fontSize = 12.sp,
                style = TextStyle(color = textColor, textAlign = TextAlign.Center)
            )
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
                        fontFamily = FontFamily.SansSerif,
                        color = AppColor.DavysGrey
                    )
                    Spacer(modifier = Modifier.width((cardWidth * 0.15).dp))
                }
            }
        }
    }
}

@Composable
fun PackageDetailCard(
    packageModel: PackageModel,
    packageDetailCardBtnEnum: PackageDetailCardBtnEnum,
    clickedBtn: () -> Unit
) {
    val imageSize = 80.dp
    val buttonHeight = 50.dp
    val roundedCornerShape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, roundedCornerShape)
            .clip(roundedCornerShape)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(
                    packageModel.imageUrl
                )
                    .crossfade(true)
                    .build(), contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(imageSize)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.height(imageSize)
            ) {
                SampleText(content = packageModel.packageName, 1, 20)
                SampleText(content = packageModel.packageComment, 3, fontSize = 12)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            val (lineRef, leftRef, rightRef) = createRefs()
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(color = AppColor.GreyTranspancy20, CircleShape)
                    .clip(CircleShape)
                    .constrainAs(lineRef) {
                        this.centerHorizontallyTo(parent, bias = 0.5f)
                    }
            )

            Column(
                modifier = Modifier
                    .constrainAs(leftRef) {
                        this.linkTo(start = parent.start, end = lineRef.start, bias = 0.5f)
                        this.linkTo(top = lineRef.top, bottom = lineRef.bottom)
                    }
                    .fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly
            ) {
                SampleText(content = "Soru Sayısı : ${packageModel.questionList.size}")
                SampleText(content = "Version Numarası : ${packageModel.version}")
            }
            Column(
                modifier = Modifier
                    .constrainAs(rightRef) {
                        this.linkTo(start = lineRef.start, end = parent.end, bias = 0.5f)
                        this.linkTo(top = lineRef.top, bottom = lineRef.bottom)
                    }
                    .fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly
            ) {
                SampleText(content = "İndirme Sayısı : ${packageModel.numberOfDownload.toInt()}")
                SampleText(content = packageModel.updatedTime)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = clickedBtn,
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.9f)
                    .height(buttonHeight),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = packageDetailCardBtnEnum.butonColor())
            ) {
                Text(text = stringResource(id = packageDetailCardBtnEnum.butonText()))
            }
        }
    }
}

@Composable
fun SampleText(content: String, maxLines: Int = 1, fontSize: Int = 10) {
    Text(
        text = content, maxLines = maxLines,
        fontSize = fontSize.sp,
        style = TextStyle(color = AppColor.DavysGrey)
    )
}

@Composable
fun BaseBackground(
    modifier: Modifier = Modifier,
    @StringRes titleId: Int,
    backClicked: () -> Unit,
    contentOnTitleBottom: (@Composable () -> Unit)? = null,
    contentOnFullScreen: (@Composable () -> Unit)? = null
) {
    if (contentOnFullScreen != null) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            contentOnFullScreen()
        }
    }
    Column(
        modifier = if (contentOnFullScreen != null) modifier
            .fillMaxSize() else modifier
            .fillMaxSize()
            .background(AppColor.BlueViolet)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CardTitle(title = stringResource(id = titleId), backClicked)
        Spacer(modifier = Modifier.height(16.dp))
        if (contentOnTitleBottom != null) contentOnTitleBottom()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SampleError(text: String, closeClicked: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(0.dp))
                    .background(
                        AppColor.White,
                        RoundedCornerShape(16.dp)
                    ),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Image(painter = painterResource(id = R.drawable.error),
                        contentDescription = "Close btn",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                closeClicked()
                            })
                }
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(150.dp),
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = text, color = AppColor.DavysGrey)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

}

@Composable
fun SampleCard(
    width: Dp,
    height: Dp,
    model: PackageDbModel,
    clicked: (() -> Unit)? = null
) {
    Box(modifier = Modifier.padding(top = 16.dp)) {
        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .clip(RoundedCornerShape(8.dp))
                .background(AppColor.WhiteSoft, RoundedCornerShape(8.dp))
                .clickable {
                    if (clicked != null) clicked()
                }
        ) {
            Column(
                Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                AsyncImage(
                    model = model.packageImage,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width((width.value * 0.7).dp)
                        .height((height.value * 0.7).dp)
                        .border(1.dp, AppColor.GreenMalachite, shape = RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                )
                // TODO: paket ismi uzunsa kesme yap ve tek satır gözükecek şekilde olsun
                Text(text = model.packageName)
            }
        }
    }
}

@Composable
fun SampleBackgroundCard(
    width: Dp,
    height: Dp,
    model: BackgroundDbModel,
    clicked: (() -> Unit)? = null
) {
    val backgroundColor = if (model.isActive) AppColor.GreenMalachite else AppColor.WhiteSoft
    Box(modifier = Modifier.padding(top = 16.dp)) {
        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .clip(RoundedCornerShape(8.dp))
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .clickable {
                    if (clicked != null) clicked()
                }
        ) {
            Column(
                Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                AsyncImage(
                    model = model.packageImage,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width((width.value * 0.7).dp)
                        .height((height.value * 0.7).dp)
                        .border(1.dp, AppColor.GreenMalachite, shape = RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                )
                // TODO: paket ismi uzunsa kesme yap ve tek satır gözükecek şekilde olsun
                Text(text = model.backgroundName)
            }
        }
    }
}


@Composable
fun SampleTempCard(
    width: Dp,
    height: Dp
) {
    Box(modifier = Modifier.padding(top = 16.dp)) {
        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .background(Color.Transparent)
        )
    }
}
