package com.patronusstudio.sisecevirmece2.ui.views.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.data.enums.GameMode
import com.patronusstudio.sisecevirmece2.data.utils.multiEventSend
import com.patronusstudio.sisecevirmece2.data.utils.singleEventSend
import com.patronusstudio.sisecevirmece2.ui.theme.AppColor
import com.patronusstudio.sisecevirmece2.ui.widgets.CardTitle

@Composable
fun GameTypeSelection(
    mixpanelAPI: MixpanelAPI,
    back: () -> Unit,
    gameModeSelection: (GameMode) -> Unit
) {
    val localContext = LocalContext.current
    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp
    val cardWidth = (width * 0.9).dp
    val cardHeight = (height * 0.2).dp
    val spacerHeight = (height * 0.05).dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.BlueViolet)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CardTitle(title = stringResource(R.string.select_game_type)) {
            back()
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CardButton(cardWidth, cardHeight, GameMode.NORMAL_MODE){
                    val eventName = localContext.getString(R.string.play_normal_title)
                    mixpanelAPI.singleEventSend(eventName)
                    gameModeSelection(it)
                }
                Spacer(modifier = Modifier.height(spacerHeight))
                CardButton(cardWidth, cardHeight, GameMode.SPECIAL_MODE){
                    val eventName = localContext.getString(R.string.play_special_title)
                    mixpanelAPI.singleEventSend(eventName)
                    gameModeSelection(it)
                }
            }
        }
    }
}

@Composable
private fun CardButton(width: Dp, height: Dp, gameMode: GameMode, clicked: (GameMode) -> Unit) {
    val localContext = LocalContext.current
    Card(
        modifier = Modifier
            .defaultMinSize(minHeight = height)
            .width(width)
            .clickable(onClick = { clicked(gameMode) }),
        backgroundColor = AppColor.Mustard,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(16.dp)) {
            Text(
                text = gameMode.getTitle(localContext),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle.Default.copy(
                    color = AppColor.SunsetOrange,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = gameMode.getContent(localContext),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle.Default.copy(
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}



