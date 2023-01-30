package com.patronusstudio.sisecevirmece.ui.views.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.enums.SelectableEnum
import com.patronusstudio.sisecevirmece.data.model.BaseCategoryModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.BackgroundDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.BottleDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.ProfileCategoryModel
import com.patronusstudio.sisecevirmece.data.viewModels.ProfileScreenViewModel
import com.patronusstudio.sisecevirmece.ui.screens.LoadingAnimation
import com.patronusstudio.sisecevirmece.ui.theme.AppColor
import com.patronusstudio.sisecevirmece.ui.widgets.BaseBackground
import com.patronusstudio.sisecevirmece.ui.widgets.SampleBackgroundCard
import com.patronusstudio.sisecevirmece.ui.widgets.SampleCard
import com.patronusstudio.sisecevirmece.ui.widgets.SampleTempCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(backClicked: () -> Unit) {

    val viewModel = hiltViewModel<ProfileScreenViewModel>()
    val coroutineScope = rememberCoroutineScope()
    val packageCardWidth = (LocalConfiguration.current.screenWidthDp * 0.4).dp
    val packageCardHeight = (LocalConfiguration.current.screenHeightDp * 0.25).dp
    LaunchedEffect(key1 = Unit, block = {
        viewModel.getPackages()
    })

    BaseBackground(titleId = R.string.my_profile, backClicked = backClicked) {
        Titles(list = viewModel.titles.collectAsState().value) {
            viewModel.clickedBtn(it.id)
            viewModel.setTitle(it)
            coroutineScope.launch {
                viewModel.getDatas(it)
            }
        }
        AnimatedVisibility(
            visible = viewModel.packages.collectAsState().value.isNotEmpty(),
            enter = fadeIn() + slideInVertically {
                it / 2
            }, exit = fadeOut()
        ) {
            Packages(
                viewModel.packages.collectAsState().value, packageCardWidth, packageCardHeight
            )
        }
        AnimatedVisibility(
            visible = viewModel.bottles.collectAsState().value.isNotEmpty(),
            enter = fadeIn() + slideInVertically {
                it / 2
            }, exit = fadeOut()
        ) {
            val bottleCardSize = (LocalConfiguration.current.screenWidthDp * 0.25).dp
            Bottles(
                viewModel.bottles.collectAsState().value, bottleCardSize
            ) {
                coroutineScope.launch {
                    viewModel.setBottleActiveStatuOnDb(it.primaryId)
                    delay(200)
                    viewModel.setBottleActiveStatuOnLocal(it.primaryId)
                }
            }
        }
        AnimatedVisibility(
            visible = viewModel.backgrounds.collectAsState().value.isNotEmpty(),
            enter = fadeIn() + slideInVertically {
                it / 2
            }, exit = fadeOut()
        ) {
            Backgrounds(
                viewModel.backgrounds.collectAsState().value, packageCardWidth, packageCardHeight
            ){
                coroutineScope.launch {
                    viewModel.setBackgroundActiveStatuOnDb(it.primaryId)
                    delay(200)
                    viewModel.setBackgroundActiveStatuOnLocal(it.primaryId)
                }
            }
        }
        AnimatedVisibility(visible = viewModel.isLoading.collectAsState().value) {
            LoadingAnimation()
        }
    }
}

@Composable
private fun Titles(list: List<BaseCategoryModel>, clicked: (ProfileCategoryModel) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(),
            content = {
                items(
                    items = list
                ) { item: BaseCategoryModel ->
                    Spacer(modifier = Modifier.width(16.dp))
                    PackageSelectButton(item = item, clicked = {
                        clicked(item as ProfileCategoryModel)
                    }, content = {
                        item as ProfileCategoryModel
                        val textColor = Color(
                            android.graphics.Color.parseColor(
                                if (item.isSelected == SelectableEnum.YES) item.activeTextColor
                                else item.passiveTextColor
                            )
                        )
                        Text(text = item.name, color = textColor)
                    })

                }
                item {
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Packages(
    packages: List<PackageDbModel>,
    packageCardWidth: Dp,
    packageCardHeight: Dp
) {
    FlowRow(
        maxItemsInEachRow = 2,
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        packages.forEachIndexed { index, packageDbModel ->
            SampleCard(
                width = packageCardWidth,
                height = packageCardHeight,
                model = packageDbModel
            )
            if (index == packages.size - 1) {
                SampleTempCard(packageCardWidth, packageCardHeight)
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Bottles(
    packages: List<BottleDbModel>,
    cardSize: Dp,
    clicked: (BottleDbModel) -> Unit
) {
    FlowRow(
        maxItemsInEachRow = 3,
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        packages.forEach {
            BottleCard(cardSize, it, clicked)
        }
        if (packages.size % 3 == 1) {
            SampleBottleCard(cardSize)
            SampleBottleCard(cardSize)
        } else if (packages.size % 3 == 2) {
            SampleBottleCard(cardSize)
        }
    }
}

@Composable
private fun BottleCard(cardSize: Dp, model: BottleDbModel, clicked: (BottleDbModel) -> Unit) {
    val shape = RoundedCornerShape(4.dp)
    val backgroundColor = if (model.isActive) AppColor.GreenMalachite else AppColor.WhiteSoft
    Box(modifier = Modifier.padding(top = 16.dp)) {
        Box(
            modifier = Modifier
                .size(cardSize)
                .clip(shape)
                .background(backgroundColor, shape)
                .clickable {
                    if (model.isActive.not()) clicked(model)
                }, contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = model.packageImage, contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size((cardSize.value * 0.9).dp)
            )
        }
    }
}

@Composable
private fun SampleBottleCard(cardSize: Dp) {
    val shape = RoundedCornerShape(4.dp)
    Box(modifier = Modifier.padding(top = 16.dp)) {
        Box(
            modifier = Modifier
                .size(cardSize)
                .clip(shape)
                .background(Color.Transparent, shape)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Backgrounds(
    backgrounds: List<BackgroundDbModel>,
    packageCardWidth: Dp,
    packageCardHeight: Dp, clicked: (BackgroundDbModel) -> Unit
) {
    FlowRow(
        maxItemsInEachRow = 2,
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        backgrounds.forEachIndexed { index, backgroundDbModel ->
            SampleBackgroundCard(
                width = packageCardWidth,
                height = packageCardHeight,
                model = backgroundDbModel
            ){
                clicked(backgroundDbModel)
            }
            if (index == backgrounds.size - 1) {
                SampleTempCard(packageCardWidth, packageCardHeight)
            }
        }
    }
}





















