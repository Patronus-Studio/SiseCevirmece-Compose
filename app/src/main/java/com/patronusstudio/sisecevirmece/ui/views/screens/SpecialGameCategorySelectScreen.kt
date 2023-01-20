package com.patronusstudio.sisecevirmece.ui.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.viewModels.SpecialGameCategorySelectViewModel
import com.patronusstudio.sisecevirmece.ui.theme.AppColor
import com.patronusstudio.sisecevirmece.ui.widgets.BaseBackground
import com.patronusstudio.sisecevirmece.ui.widgets.CardImageWithText

//hiç oynanacak paketi yoksa uyarı bas ekranda
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpecialGameCategorySelectScreen(backClicked: () -> Unit, passGameScreen: () -> Unit) {
    val viewModel = hiltViewModel<SpecialGameCategorySelectViewModel>()
    val packages = viewModel.packages.collectAsState().value
    val cardWidth = (LocalConfiguration.current.screenWidthDp * 0.3).dp
    val cardHeight = (LocalConfiguration.current.screenHeightDp * 0.20).dp
    val imageSize = (LocalConfiguration.current.screenHeightDp * 0.12).dp
    LaunchedEffect(key1 = Unit, block = {
        viewModel.getAllPackages()
    })
    BaseBackground(titleId = R.string.select_game_category, backClicked = backClicked) {
        FlowRow(
            modifier = Modifier
                .fillMaxSize(), maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            packages.forEach {
                CardImageWithText(
                    image = it.packageImage!!,
                    text = it.packageName,
                    backgroundColor = AppColor.White,
                    textColor = AppColor.DavysGrey,
                    cardSizeWidth = cardWidth,
                    cardSizeHeight = cardHeight,
                    imageSize = imageSize,
                    borderColor = AppColor.SeaSerpent
                ) {

                }
            }
        }
    }
}