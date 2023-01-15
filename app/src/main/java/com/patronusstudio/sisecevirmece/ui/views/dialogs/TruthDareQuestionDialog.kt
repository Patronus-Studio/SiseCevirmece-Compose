package com.patronusstudio.sisecevirmece.ui.views.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.ui.theme.AppColor
import com.patronusstudio.sisecevirmece.ui.widgets.AutoTextSize

@Composable
fun TruthDareQuestionDialog(closeClicked: () -> Unit) {
    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp
    val smallCardHeight = (height * 0.06).dp
    val smallPaddingHeight = (height * 0.03).dp

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp))
        TitleCard((width * 0.9).dp)
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GeneralCard(
                    (width * 0.9).dp, (height * 0.2).dp,
                    text = stringResource(id = R.string.lorem_ipsum_single_line),
                    cardPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(smallPaddingHeight))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    GeneralCard(
                        width = (width * 0.4).dp,
                        smallCardHeight,
                        text = stringResource(R.string.replied),
                        clicked = closeClicked
                    )
                    GeneralCard(
                        width = (width * 0.4).dp,
                        smallCardHeight,
                        text = stringResource(R.string.change_question),
                        clicked = {

                        })
                }
                Spacer(modifier = Modifier.height(smallPaddingHeight))
                GeneralCard(
                    (width * 0.9).dp,
                    smallCardHeight,
                    text = stringResource(R.string.i_wil_ask_question),
                    clicked = closeClicked
                )
            }
        }
    }
}

@Composable
private fun TitleCard(width: Dp) {
    Card(
        modifier = Modifier
            .width(width)
            .padding(vertical = 8.dp), backgroundColor = AppColor.Mustard,
        shape = RoundedCornerShape(16.dp), elevation = 4.dp
    ) {
        Text(
            text = "Doğruluk",
            style = TextStyle.Default.copy(
                color = AppColor.SunsetOrange,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
            ), modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun GeneralCard(
    width: Dp,
    height: Dp,
    text: String,
    clicked: (() -> Unit)? = null,
    cardPadding: PaddingValues = PaddingValues(0.dp)
) {
    val modifier = if (clicked == null) Modifier
        .width(width)
        .height(height)
    else Modifier
        .width(width)
        .height(height)
        .clickable(onClick = clicked)
    Card(
        modifier = modifier,
        backgroundColor = AppColor.Mustard,
        shape = RoundedCornerShape(16.dp), elevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = cardPadding),
            contentAlignment = Alignment.Center
        ) {
            AutoTextSize(
                text = text,
                textStyle = TextStyle.Default.copy(
                    color = AppColor.SunsetOrange,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                ), maxLines = 10
            )
        }
    }
}