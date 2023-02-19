package com.patronusstudio.sisecevirmece2.ui.views.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece2.data.utils.BetmRounded
import com.patronusstudio.sisecevirmece2.data.utils.multiEventSend
import com.patronusstudio.sisecevirmece2.data.viewModels.SpecialGameCategorySelectViewModel
import com.patronusstudio.sisecevirmece2.ui.theme.AppColor
import com.patronusstudio.sisecevirmece2.ui.widgets.BaseBackground
import com.patronusstudio.sisecevirmece2.ui.widgets.CardImageWithText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpecialGameCategorySelectScreen(
    mixpanelAPI: MixpanelAPI,
    backClicked: () -> Unit,
    passGameScreen: (String) -> Unit
) {
    val localContext = LocalContext.current
    val viewModel = hiltViewModel<SpecialGameCategorySelectViewModel>()
    val cardWidth = (LocalConfiguration.current.screenWidthDp * 0.3).dp
    val cardHeight = (LocalConfiguration.current.screenHeightDp * 0.20).dp
    val imageSize = (LocalConfiguration.current.screenHeightDp * 0.12).dp
    val spaceSizeCards = ((LocalConfiguration.current.screenWidthDp.dp) - (cardWidth * 2)) / 3
    val emptyPackageImageSize = (LocalConfiguration.current.screenWidthDp * 0.75).dp
    val playButtonWidth = (LocalConfiguration.current.screenWidthDp * 0.9).dp
    val isFirstInit = remember {
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = Unit, block = {
        viewModel.clearData()
        viewModel.getAllPackages()
        isFirstInit.value = false
    })
    BaseBackground(
        titleId = R.string.select_game_category,
        backClicked = backClicked,
        contentOnTitleBottom = {
            FlowRow(
                maxItemsInEachRow = 2,
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                viewModel.packages.forEachIndexed { index, packageDbModel ->
                    SingleCard(model = packageDbModel, clicked = {
                        viewModel.setShowStatu(packageDbModel)
                        viewModel.setSelectedPackages()
                    }, cardWidth, cardHeight, imageSize)
                    if (index == viewModel.packages.size - 1) {
                        SingleTempCard(cardWidth = cardWidth, cardHeight = cardHeight)
                    }
                }
            }
        })
    PlayButton(
        selectedPackageSize = viewModel.selectedPackage.size,
        playButtonWidth = playButtonWidth
    ) {
        val events = mutableMapOf<String, String>()
        viewModel.packages.forEach {
            events["Selected package"] = it.packageName
        }
        mixpanelAPI.multiEventSend(
            localContext.getString(R.string.special_game_mode_selected_package),
            events
        )
        passGameScreen(viewModel.getSelectedPackageJSON())
    }
    AnimationDialog(
        packageSize = viewModel.packages.size,
        isFirstInit.value,
        emptyPackageImageSize
    ) {
        isFirstInit.value = true
        backClicked()
    }
}

@Composable
private fun SingleCard(
    model: PackageDbModel,
    clicked: () -> Unit,
    cardWidth: Dp,
    cardHeight: Dp,
    imageSize: Dp
) {
    Box(modifier = Modifier.padding(top = 16.dp)) {
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
private fun SingleTempCard(
    cardWidth: Dp,
    cardHeight: Dp
) {
    Spacer(
        modifier = Modifier
            .padding(top = 16.dp)
            .width(cardWidth)
            .height(cardHeight)
    )
}

@Composable
private fun PlayButton(selectedPackageSize: Int, playButtonWidth: Dp, clicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp), contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = selectedPackageSize > 0,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            Button(
                onClick = clicked,
                colors = ButtonDefaults.buttonColors(backgroundColor = AppColor.SunsetOrange),
                modifier = Modifier.width(playButtonWidth)
            ) {
                Text(
                    text = stringResource(R.string.play_bigger),
                    textAlign = TextAlign.Center,
                    color = AppColor.White,
                    fontSize = 24.sp,fontFamily = BetmRounded,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun AnimationDialog(
    packageSize: Int,
    isFirstInit: Boolean,
    emptyPackageImageSize: Dp,
    dissmisRequest: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.package_empty))
    if (packageSize <= 3 && !isFirstInit) {
        Dialog(
            onDismissRequest = dissmisRequest,
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
                    text = stringResource(
                        if (packageSize == 0) R.string.paket_doesnt_find_to_play
                        else R.string.at_least_5_package_add_to_play
                    ),
                    color = AppColor.White,
                    fontSize = 22.sp,
                    fontFamily = BetmRounded,
                    fontWeight = FontWeight.Normal                )
            }
        }
    }

}