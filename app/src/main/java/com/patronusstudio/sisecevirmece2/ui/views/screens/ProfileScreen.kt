package com.patronusstudio.sisecevirmece2.ui.views.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.patronusstudio.sisecevirmece2.BuildConfig
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.data.enums.AnimMillis
import com.patronusstudio.sisecevirmece2.data.enums.InterstitialAdViewLoadStatusEnum
import com.patronusstudio.sisecevirmece2.data.enums.PackageDetailButtonEnum
import com.patronusstudio.sisecevirmece2.data.enums.SelectableEnum
import com.patronusstudio.sisecevirmece2.data.model.BaseCategoryModel
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.BackgroundDbModel
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.BottleDbModel
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.ProfileCategoryModel
import com.patronusstudio.sisecevirmece2.data.utils.BetmRounded
import com.patronusstudio.sisecevirmece2.data.utils.getActivity
import com.patronusstudio.sisecevirmece2.data.utils.multiEventSend
import com.patronusstudio.sisecevirmece2.data.utils.showSample
import com.patronusstudio.sisecevirmece2.data.viewModels.ProfileScreenViewModel
import com.patronusstudio.sisecevirmece2.ui.screens.LoadingAnimation
import com.patronusstudio.sisecevirmece2.ui.theme.AppColor
import com.patronusstudio.sisecevirmece2.ui.views.dialogs.ProfilePackageCard
import com.patronusstudio.sisecevirmece2.ui.widgets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(mixpanelAPI: MixpanelAPI, backClicked: () -> Unit) {
    val localContext = LocalContext.current
    val viewModel = hiltViewModel<ProfileScreenViewModel>()
    val coroutineScope = rememberCoroutineScope()
    val packageCardWidth = (LocalConfiguration.current.screenWidthDp * 0.4).dp
    val packageCardHeight = (LocalConfiguration.current.screenHeightDp * 0.25).dp
    LaunchedEffect(key1 = Unit, block = {
        viewModel.getPackages()
    })

    BaseBackground(
        titleId = R.string.my_profile,
        backClicked = backClicked,
        contentOnTitleBottom = {
            Titles(list = viewModel.titles.collectAsState().value) {
                viewModel.clickedBtn(it.id)
                viewModel.setTitle(it)
                coroutineScope.launch {
                    viewModel.getDatas(it)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(
                visible = viewModel.packages.collectAsState().value.isNotEmpty(),
                enter = fadeIn() + slideInVertically {
                    it / 2
                }, exit = fadeOut()
            ) {
                Packages(
                    packageCardWidth, packageCardHeight, viewModel
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
                ) { bottleDbModel ->
                    viewModel.setLoadingStatus(true)
                    InterstitialAdView.loadInterstitial(
                        localContext.getActivity(),
                        BuildConfig.package_download_interstitial
                    ) { ad ->
                        if (ad == InterstitialAdViewLoadStatusEnum.SHOWED) {
                            viewModel.setLoadingStatus(false)
                        } else if (ad == InterstitialAdViewLoadStatusEnum.DISSMISSED) {
                            viewModel.setLoadingStatus(false)
                            coroutineScope.launch(Dispatchers.Main) {
                                viewModel.setBottleActiveStatuOnDb(bottleDbModel.primaryId)
                                viewModel.setBottleActiveStatuOnLocal(bottleDbModel.primaryId)
                            }
                        } else {
                            localContext.showSample()
                            viewModel.setLoadingStatus(false)
                        }
                    }

                    val eventName = localContext.getString(R.string.bottles)
                    val events = mapOf(
                        Pair(
                            localContext.getString(R.string.played_bottles),
                            bottleDbModel.bottleName
                        )
                    )
                    mixpanelAPI.multiEventSend(eventName, events)
                }
            }
            AnimatedVisibility(
                visible = viewModel.backgrounds.collectAsState().value.isNotEmpty(),
                enter = fadeIn() + slideInVertically {
                    it / 2
                }, exit = fadeOut()
            ) {
                Backgrounds(
                    viewModel.backgrounds.collectAsState().value,
                    packageCardWidth,
                    packageCardHeight
                ) { backgroundDbModel ->
                    viewModel.setLoadingStatus(true)
                    InterstitialAdView.loadInterstitial(
                        localContext.getActivity(),
                        BuildConfig.package_download_interstitial
                    ) { ad ->
                        if (ad == InterstitialAdViewLoadStatusEnum.SHOWED) {
                            viewModel.setLoadingStatus(false)
                        } else if (ad == InterstitialAdViewLoadStatusEnum.DISSMISSED) {
                            viewModel.setLoadingStatus(false)
                            viewModel.setBackgroundActiveStatuOnDb(backgroundDbModel)
                            viewModel.setBackgroundActiveStatuOnLocal(backgroundDbModel)
                        } else {
                            localContext.showSample()
                            viewModel.setLoadingStatus(false)
                        }
                    }
                    val eventName = localContext.getString(R.string.bottles)
                    val events =
                        mapOf(
                            Pair(
                                localContext.getString(R.string.played_bottles),
                                backgroundDbModel.backgroundName
                            )
                        )
                    mixpanelAPI.multiEventSend(eventName, events)
                }
            }
            AnimatedVisibility(visible = viewModel.isLoading.collectAsState().value) {
                LoadingAnimation()
            }
        })
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
                        Text(
                            text = item.name, color = textColor, fontFamily = BetmRounded,
                            fontWeight = FontWeight.Normal
                        )
                    })

                }
                item {
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun Packages(
    packageCardWidth: Dp,
    packageCardHeight: Dp,
    viewModel: ProfileScreenViewModel
) {
    val scroolState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val packageDialogIsOpened = remember { mutableStateOf(false) }
    val showQuestionDialogIsOpened = remember { mutableStateOf(false) }
    FlowRow(
        maxItemsInEachRow = 2,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroolState),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        viewModel.packages.collectAsState().value.forEachIndexed { index, packageDbModel ->
            SampleCard(
                width = packageCardWidth,
                height = packageCardHeight,
                model = packageDbModel
            ) {
                coroutineScope.launch {
                    packageDialogIsOpened.value = true
                    delay(AnimMillis.VERY_SHORT.millis.toLong())
                    viewModel.setSelectedPackageModel(packageDbModel)
                }
            }
            if (index == viewModel.packages.collectAsState().value.size - 1) {
                SampleTempCard(packageCardWidth, packageCardHeight)
            }
        }
    }
    if (packageDialogIsOpened.value) {
        Dialog(
            onDismissRequest = {
                if (viewModel.selectedPackage.value != null) {
                    coroutineScope.launch {
                        viewModel.setSelectedPackageModel(null)
                    }
                } else if (showQuestionDialogIsOpened.value) {
                    showQuestionDialogIsOpened.value = false
                }
                coroutineScope.launch {
                    delay(AnimMillis.NORMAL.millis.toLong())
                    packageDialogIsOpened.value = false
                }
            },
            DialogProperties(usePlatformDefaultWidth = false)
        ) {
            AnimatedVisibility(
                visible = viewModel.selectedPackage.collectAsState().value != null,
                enter = slideInVertically(
                    tween(AnimMillis.NORMAL.millis, easing = FastOutLinearInEasing),
                    initialOffsetY = { it / 2 }) + fadeIn(tween(AnimMillis.NORMAL.millis)),
                exit = slideOutVertically(
                    tween(AnimMillis.NORMAL.millis, easing = FastOutLinearInEasing),
                    targetOffsetY = { it / 2 }) + fadeOut(tween(AnimMillis.NORMAL.millis))
            ) {
                ConstraintLayout(Modifier.fillMaxSize()) {
                    val (popupRef) = createRefs()
                    Box(modifier = Modifier
                        .wrapContentHeight()
                        .constrainAs(popupRef) {
                            this.bottom.linkTo(parent.bottom)
                            this.start.linkTo(parent.start)
                            this.end.linkTo(parent.end)
                        }) {
                        ProfilePackageCard(viewModel) {
                            if (it == PackageDetailButtonEnum.REMOVE_PACKAGE) {
                                coroutineScope.launch {
                                    viewModel.removePackage()
                                    viewModel.setSelectedPackageModel(null)
                                    delay(AnimMillis.NORMAL.millis.toLong())
                                    packageDialogIsOpened.value = false
                                }
                            } else if (it == PackageDetailButtonEnum.SHOW_QUESTION) {
                                coroutineScope.launch {
                                    viewModel.getQuestions()
                                    viewModel.setSelectedPackageModel(null)
                                    delay(AnimMillis.NORMAL.millis.toLong())
                                    showQuestionDialogIsOpened.value = true
                                }
                            }
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = showQuestionDialogIsOpened.value,
                enter = slideInVertically(
                    tween(AnimMillis.NORMAL.millis, easing = FastOutLinearInEasing),
                    initialOffsetY = { it / 2 }) + fadeIn(tween(AnimMillis.NORMAL.millis)),
                exit = slideOutVertically(
                    tween(AnimMillis.NORMAL.millis, easing = FastOutLinearInEasing),
                    targetOffsetY = { it / 2 }) + fadeOut(tween(AnimMillis.NORMAL.millis))
            ) {
                val localHeight = LocalConfiguration.current.screenHeightDp.dp
                val questions = viewModel.questions.collectAsState().value
                Box(
                    modifier = Modifier
                        .requiredHeightIn(max = (localHeight.value * 0.9).dp)
                        .wrapContentHeight()
                        .fillMaxWidth(0.9f)
                        .background(AppColor.White, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .padding(top = 16.dp, end = 16.dp, start = 16.dp)
                ) {
                    Column {
                        LazyColumn(modifier = Modifier
                            .requiredHeightIn(max = (localHeight.value * 0.75).dp)
                            .wrapContentHeight(), content = {
                            items(questions) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = it.question,
                                        modifier = Modifier.weight(0.7f),
                                        color = AppColor.DavysGrey,fontFamily = BetmRounded,
                                        fontWeight = FontWeight.Normal
                                    )
                                    if (it.isShowed == 1) {
                                        AsyncImage(
                                            model = R.drawable.tick,
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(16.dp)
                                                .weight(0.05f)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        })
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(AppColor.BlueViolet, RoundedCornerShape(12.dp))
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    coroutineScope.launch {
                                        viewModel.resetQuestions()
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.reset_questions),
                                color = AppColor.White,
                                fontFamily = BetmRounded,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
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
    val scroolState = rememberScrollState()
    FlowRow(
        maxItemsInEachRow = 3,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scroolState),
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
private fun BottleCard(
    cardSize: Dp,
    model: BottleDbModel,
    clicked: (BottleDbModel) -> Unit
) {
    val shape = RoundedCornerShape(4.dp)
    val backgroundColor =
        if (model.isActive) AppColor.GreenMalachite else AppColor.WhiteSoft
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
    val scroolState = rememberScrollState()
    FlowRow(
        maxItemsInEachRow = 2,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scroolState),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        backgrounds.forEachIndexed { index, backgroundDbModel ->
            SampleBackgroundCard(
                width = packageCardWidth,
                height = packageCardHeight,
                model = backgroundDbModel
            ) {
                clicked(backgroundDbModel)
            }
            if (index == backgrounds.size - 1) {
                SampleTempCard(packageCardWidth, packageCardHeight)
            }
        }
    }
}





















