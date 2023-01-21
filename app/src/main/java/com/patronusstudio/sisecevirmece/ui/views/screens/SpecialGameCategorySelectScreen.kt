package com.patronusstudio.sisecevirmece.ui.views.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece.data.viewModels.SpecialGameCategorySelectViewModel
import com.patronusstudio.sisecevirmece.ui.theme.AppColor
import com.patronusstudio.sisecevirmece.ui.widgets.BaseBackground
import com.patronusstudio.sisecevirmece.ui.widgets.CardImageWithText

//hiç oynanacak paketi yoksa uyarı bas ekranda
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SpecialGameCategorySelectScreen(backClicked: () -> Unit, passGameScreen: () -> Unit) {
    val viewModel = hiltViewModel<SpecialGameCategorySelectViewModel>()
    val cardWidth = (LocalConfiguration.current.screenWidthDp * 0.3).dp
    val cardHeight = (LocalConfiguration.current.screenHeightDp * 0.20).dp
    val imageSize = (LocalConfiguration.current.screenHeightDp * 0.12).dp
    val spaceSizeCards = ((LocalConfiguration.current.screenWidthDp.dp) - (cardWidth * 2)) / 3
    val emptyPackageImageSize = (LocalConfiguration.current.screenWidthDp * 0.75).dp
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.package_empty))
    LaunchedEffect(key1 = Unit, block = {
        viewModel.getAllPackages()
    })
    BaseBackground(titleId = R.string.select_game_category, backClicked = backClicked) {
        LazyColumn(content = {
            itemsIndexed(viewModel.packages) { index: Int, model: PackageDbModel ->
                if (index == 0) {
                    if (index + 1 <= viewModel.packages.size - 1) {
                        DoubleCard(
                            firstModel = model,
                            secondModel = viewModel.packages[index + 1],
                            firstClicked = {
                                viewModel.setShowStatu(model)
                            }, secondClicked = {
                                viewModel.setShowStatu(viewModel.packages[index + 1])
                            }, cardWidth, cardHeight, imageSize
                        )
                    } else {
                        SingeCard(model = model, clicked = {
                            viewModel.setShowStatu(model)
                        }, cardWidth, cardHeight, imageSize, spaceSizeCards)
                    }
                } else if (index + 2 <= viewModel.packages.size - 1) {
                    DoubleCard(
                        firstModel = viewModel.packages[index + 1],
                        secondModel = viewModel.packages[index + 2],
                        firstClicked = {
                            viewModel.setShowStatu(viewModel.packages[index + 1])
                        }, secondClicked = {
                            viewModel.setShowStatu(viewModel.packages[index + 2])
                        }, cardWidth, cardHeight, imageSize
                    )
                } else if (index + 1 <= viewModel.packages.size - 1) {
                    SingeCard(model = viewModel.packages[index + 1], clicked = {
                        viewModel.setShowStatu(viewModel.packages[index + 1])
                    }, cardWidth, cardHeight, imageSize, spaceSizeCards)
                }
            }
        })
        if (viewModel.packages.size == 0) {
            Dialog(
                onDismissRequest = {
                    backClicked()
                },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(emptyPackageImageSize),
                    )
                    Text(
                        text = stringResource(R.string.paket_doesnt_find_to_play),
                        color = AppColor.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal,
                    )
                }
            }
        }
    }
}

@Composable
private fun SingeCard(
    model: PackageDbModel,
    clicked: () -> Unit,
    cardWidth: Dp,
    cardHeight: Dp,
    imageSize: Dp,
    spaceSize: Dp
) {
    Spacer(modifier = Modifier.height(16.dp))
    Row(Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(spaceSize))
        CardImageWithText(
            image = model.packageImage!!,
            text = model.packageName,
            backgroundColor = if (model.isSelected.not()) AppColor.White else AppColor.ScreaminGreen,
            textColor = if (model.isSelected) AppColor.DavysGrey else AppColor.DavysGrey,
            cardSizeWidth = cardWidth,
            cardSizeHeight = cardHeight,
            imageSize = imageSize,
            borderColor = if (model.isSelected) AppColor.White else AppColor.SeaSerpent,
            clicked = clicked
        )
    }

}

@Composable
private fun DoubleCard(
    firstModel: PackageDbModel, secondModel: PackageDbModel,
    firstClicked: () -> Unit, secondClicked: () -> Unit,
    cardWidth: Dp, cardHeight: Dp, imageSize: Dp
) {
    Spacer(modifier = Modifier.height(16.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        CardImageWithText(
            image = firstModel.packageImage!!,
            text = firstModel.packageName,
            backgroundColor = if (firstModel.isSelected.not()) AppColor.White else AppColor.ScreaminGreen,
            textColor = if (firstModel.isSelected) AppColor.DavysGrey else AppColor.DavysGrey,
            cardSizeWidth = cardWidth,
            cardSizeHeight = cardHeight,
            imageSize = imageSize,
            borderColor = if (firstModel.isSelected) AppColor.White else AppColor.SeaSerpent,
            clicked = firstClicked
        )
        CardImageWithText(
            image = secondModel.packageImage!!,
            text = secondModel.packageName,
            backgroundColor = if (secondModel.isSelected.not()) AppColor.White else AppColor.ScreaminGreen,
            textColor = if (secondModel.isSelected) AppColor.DavysGrey else AppColor.DavysGrey,
            cardSizeWidth = cardWidth,
            cardSizeHeight = cardHeight,
            imageSize = imageSize,
            borderColor = if (secondModel.isSelected) AppColor.White else AppColor.SeaSerpent,
            clicked = secondClicked
        )

    }
}