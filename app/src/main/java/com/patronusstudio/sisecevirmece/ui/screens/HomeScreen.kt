package com.patronusstudio.sisecevirmece.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.AvatarStatu
import com.patronusstudio.sisecevirmece.data.getSamplePhotoUrl
import com.patronusstudio.sisecevirmece.data.model.Avatar
import com.patronusstudio.sisecevirmece.ui.theme.*
import com.patronusstudio.sisecevirmece.ui.widgets.CardImageWithText
import com.patronusstudio.sisecevirmece.ui.widgets.LevelBar
import com.patronusstudio.sisecevirmece.ui.widgets.UserPic

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueViolet)
    ) {
        Space(0.02)
        Title()
        Space(0.05)
        UserPicHousting()
        Space(0.02)
        Username()
        LevelBar()
        Space(0.03)
        HomeCards()
        Space(0.05)
        PlayButton()
    }
}

@Composable
private fun Title() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.app_name_with_blank),
            style = TextStyle(
                color = Color.White,
                shadow = Shadow(
                    DavysGrey, offset = Offset(0f, 0f), blurRadius = 16f
                )
            ),
            fontSize = 24.sp, fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun Space(ratio: Double) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val spaceSize = (screenWidth * ratio).dp
    Spacer(modifier = Modifier.height(spaceSize))
}

@Composable
private fun UserPicHousting() {
    val currentImage = remember { mutableStateOf(getSamplePhotoUrl()) }
    currentImage.value.statu = AvatarStatu.BUYED
    val isClicked = remember { mutableStateOf(false) }
    if (isClicked.value) {
        OpenDialog {
            isClicked.value = false
            if (it != null) currentImage.value = it
        }
    }
    UserPic(0.4, currentImage.value) {
        isClicked.value = true
    }
}

@Composable
private fun Username(username: String = "Süleyman Sezer") {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(text = username, fontSize = 16.sp, style = TextStyle(color = Mustard))
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

//fun OpenDialog(isClicked: MutableState<Boolean>) {
@Composable
fun OpenDialog(dismiss: (Avatar?) -> Unit) {
    //AnimatedVisibility(visible = isClicked.value) {
    val list = List(11) {
        getSamplePhotoUrl()
    }
    val width = (LocalConfiguration.current.screenWidthDp * 0.8).dp
    val height = (LocalConfiguration.current.screenHeightDp * 0.6).dp
    AnimatedVisibility(visible = true) {
        Dialog(
            onDismissRequest = {
                dismiss(null)
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(shape = RoundedCornerShape(16.dp)) {
                LazyVerticalGrid(modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .width(width)
                    .background(Color.White),
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                    content = {
                        itemsIndexed(list, itemContent = { position, itemValue ->
                            Box(
                                Modifier
                                    .wrapContentSize()
                                    .padding(vertical = 8.dp)
                            ) {
                                UserPic(ratio = 0.25, avatar = itemValue) {
                                    if (itemValue.statu == AvatarStatu.BUYED) {
                                        dismiss(itemValue)
                                    }
                                }
                            }
                        })
                    })
            }
        }
    }
}
