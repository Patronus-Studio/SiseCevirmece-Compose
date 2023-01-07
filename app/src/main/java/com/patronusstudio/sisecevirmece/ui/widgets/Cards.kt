package com.patronusstudio.sisecevirmece.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.enums.PackageDetailCardBtnEnum
import com.patronusstudio.sisecevirmece.data.model.PackageModel
import com.patronusstudio.sisecevirmece.ui.theme.AppColor

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
fun PackageDetailCard(
    packageModel: PackageModel,
    packageDetailCardBtnEnum: PackageDetailCardBtnEnum,
    clickedBtn: () -> Unit
) {
    val imageSize = 64.dp
    val buttonHeight = 50.dp
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(16.dp)
    ) {
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
                SampleText(content = packageModel.packageName, 1, 16)
                SampleText(content = packageModel.packageComment, 3)
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
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
                        this.linkTo(start = parent.start, end = lineRef.start, bias = 0.9f)
                        this.linkTo(top = lineRef.top, bottom = lineRef.bottom)
                    }
                    .fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly
            ) {
                SampleText(content = packageModel.numberOfDownload.toInt().toString())
                SampleText(content = packageModel.version.toInt().toString())
            }

            Column(
                modifier = Modifier
                    .constrainAs(rightRef) {
                        this.linkTo(start = lineRef.end, end = parent.end)
                        this.linkTo(top = lineRef.top, bottom = lineRef.bottom)
                    }
                    .fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly
            ) {
                SampleText(content = packageModel.numberOfLike.toInt().toString())
                SampleText(content = packageModel.updatedTime)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
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


