package com.patronusstudio.sisecevirmece.ui.views.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import com.patronusstudio.sisecevirmece.MainApplication
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.enums.InAppScreenNavEnums
import com.patronusstudio.sisecevirmece.data.model.AvatarModel
import com.patronusstudio.sisecevirmece.data.viewModels.HomeViewModel
import com.patronusstudio.sisecevirmece.ui.screens.LoadingAnimation
import com.patronusstudio.sisecevirmece.ui.theme.AppColor
import com.patronusstudio.sisecevirmece.ui.widgets.CardImageWithText
import com.patronusstudio.sisecevirmece.ui.widgets.ErrorSheet
import com.patronusstudio.sisecevirmece.ui.widgets.UserPic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(route: (InAppScreenNavEnums) -> Unit) {
    val mContext = LocalContext.current
    val viewModel = hiltViewModel<HomeViewModel>()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutine = rememberCoroutineScope()
    val popupClicked = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        withContext(Dispatchers.IO) {
            viewModel.getUserGameInfo(MainApplication.authToken)
        }
        withContext(Dispatchers.IO) {
            viewModel.getAvatars()
            viewModel.truthDareControl()
            viewModel.bottleControl()
            viewModel.backgroundControl()
        }

    }
    LaunchedEffect(
        key1 = viewModel.loginError.collectAsState().value
    ) {
        if (viewModel.loginError.value.isNotEmpty()) {
            if (sheetState.isVisible.not()) sheetState.show()
        } else {
            sheetState.hide()
            //viewModel.getAllLevel()
        }
    }
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
        sheetContent = {
            ErrorSheet(
                message = viewModel.loginError.collectAsState().value,
                errorIconClicked = {
                    coroutine.launch(Dispatchers.Main) {
                        withContext(Dispatchers.IO) {
                            viewModel.clearAuthToken(mContext)
                        }
                        route(InAppScreenNavEnums.LOGOUT)
                    }
                })

        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColor.BlueViolet)
            ) {
                Space(0.02)
                Title()
                Space(0.05)
                UserPicHousting(viewModel)
                Space(0.05)
                Username(viewModel.userGameInfoModel.collectAsState().value?.username ?: "")
                /*LevelBar(
                    currentStar = viewModel.userGameInfoModel.collectAsState().value?.starCount
                        ?: 0,
                    currentLevel = viewModel.userGameInfoModel.collectAsState().value?.level.toString(),
                    nextLevelNeedStar = viewModel.calculateNextLevelStarSize(
                        viewModel.levels.collectAsState().value ?: listOf()
                    )
                )*/
                Space(0.03)
                HomeCards(route)
                Space(0.05)
                PlayButton {
                    when (it) {
                        InAppScreenNavEnums.PLAY_GAME -> {
                            route(it)
                        }
                        else -> ""
                    }
                }
                TwoButtons(dialogClicked = {
                    popupClicked.value = true
                }, route = {
                    coroutine.launch(Dispatchers.Main) {
                        withContext(Dispatchers.IO) {
                            viewModel.clearAuthToken(mContext)
                        }
                        route(InAppScreenNavEnums.LOGOUT)
                    }
                })
                if (popupClicked.value) UserComment { popupClicked.value = false }
                PatronusStudio()
                AnimatedVisibility(visible = viewModel.isLoading.collectAsState().value) {
                    LoadingAnimation()
                }
            }
        })
}

@Composable
private fun Title() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.app_name_with_blank),
            style = TextStyle(
                color = Color.White,
                shadow = Shadow(
                    AppColor.DavysGrey, offset = Offset(0f, 0f), blurRadius = 16f
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
private fun UserPicHousting(viewModel: HomeViewModel) {
    val currentImage = remember { mutableStateOf(viewModel.getCurrentAvatar()) }
    LaunchedEffect(key1 = viewModel.avatar.collectAsState().value, block = {
        currentImage.value = viewModel.getCurrentAvatar()
    })
    val isClicked = remember { mutableStateOf(false) }
    if (isClicked.value) {
        OpenDialog(viewModel) {
            isClicked.value = false
            if (it != null) {
                currentImage.value = it
                viewModel.setCurrentAvatar(it.id)
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.updateAvatar(it.id)
                }

            }
        }
    }
    UserPic(0.6, currentImage.value ?: AvatarModel(0, "")) {
        isClicked.value = true
    }
}

@Composable
private fun Username(username: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(text = username, fontSize = 16.sp, style = TextStyle(color = AppColor.Mustard))
    }
}

@Composable
private fun HomeCards(route: (InAppScreenNavEnums) -> Unit) {
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
            context.resources.getDrawable(R.drawable.category, null),
            stringResource(id = R.string.add_category),
            backgroundColor = Color.White,
            textColor = AppColor.Heliotrope,
            borderColor = AppColor.SeaSerpent,
            cardSizeWidth,
            cardSizeHeight,
            imageSize
        ) {
            route(InAppScreenNavEnums.ADD_CATEGORIES)
        }
        CardImageWithText(
            context.resources.getDrawable(R.drawable.store),
            stringResource(id = R.string.store),
            backgroundColor = AppColor.Mustard,
            imageSize = imageSize,
            textColor = AppColor.SunsetOrange,
            cardSizeWidth = cardSizeWidth,
            cardSizeHeight = cardSizeHeight
        ) {
            route(InAppScreenNavEnums.STORES)
        }
        CardImageWithText(
            context.resources.getDrawable(R.drawable.profile),
            stringResource(id = R.string.my_profile),
            backgroundColor = Color.White,
            textColor = AppColor.SunsetOrange,
            borderColor = AppColor.SeaSerpent,
            cardSizeWidth,
            cardSizeHeight,
            imageSize
        ) {
            route(InAppScreenNavEnums.PROFILE)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayButton(playGame: (InAppScreenNavEnums) -> Unit) {
    val width = LocalConfiguration.current.screenWidthDp
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Card(
            modifier = Modifier
                .width(width = (width * 0.8).dp)
                .padding(bottom = 16.dp)
                .combinedClickable(onClick = {
                    playGame(InAppScreenNavEnums.PLAY_GAME)
                }),
            backgroundColor = AppColor.Mustard,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.play), fontSize = 64.sp,
                style = TextStyle(color = AppColor.SunsetOrange), textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun OpenDialog(viewModel: HomeViewModel, dismiss: (AvatarModel?) -> Unit) {
    //AnimatedVisibility(visible = isClicked.value) {
    val width = (LocalConfiguration.current.screenWidthDp * 0.8).dp
    val height = (LocalConfiguration.current.screenHeightDp * 0.6).dp
    val avatarList = viewModel.avatar.collectAsState().value
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
                        itemsIndexed(
                            avatarList ?: arrayListOf(),
                            itemContent = { position, itemValue ->
                                Box(
                                    Modifier
                                        .wrapContentSize()
                                        .padding(vertical = 8.dp)
                                ) {
                                    UserPic(ratio = 0.25, avatar = itemValue) {
                                        //if (itemValue.buyedStatu == AvatarStatu.BUYED) {
                                        //    dismiss(itemValue)
                                        //}
                                        dismiss(itemValue)
                                    }
                                }
                            })
                    })
            }
        }
    }
}

@Composable
private fun TwoButtons(dialogClicked: () -> Unit, route: (InAppScreenNavEnums) -> Unit) {
    val width = LocalConfiguration.current.screenWidthDp
    val cardSizeWidth = (width * 0.2).dp
    val cardSizeHeight = (width * 0.2).dp
    val imageSize = (width * 0.1).dp
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        CardImageWithText(
            context.resources.getDrawable(R.drawable.comment, null),
            stringResource(R.string.user_comment),
            backgroundColor = AppColor.White,
            textColor = AppColor.Heliotrope,
            borderColor = AppColor.SeaSerpent,
            cardSizeWidth,
            cardSizeHeight,
            imageSize
        ) {
            dialogClicked()
        }
        Spacer(modifier = Modifier.width(16.dp))
        CardImageWithText(
            context.resources.getDrawable(R.drawable.logout),
            stringResource(R.string.logout),
            backgroundColor = AppColor.White,
            imageSize = imageSize,
            textColor = AppColor.SunsetOrange,
            borderColor = AppColor.SeaSerpent,
            cardSizeWidth = cardSizeWidth,
            cardSizeHeight = cardSizeHeight
        ) {
            route(InAppScreenNavEnums.LOGOUT)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun UserComment(dismiss: () -> Unit) {
    val width = (LocalConfiguration.current.screenWidthDp * 0.9).dp
    val height = (LocalConfiguration.current.screenHeightDp * 0.45).dp
    val outlinedTextHeight = (height.value * 0.4).dp
    val buttonHeight = (height.value * 0.12).dp
    val starWidth = (width.value * 0.1).dp
    var rating: Float by remember { mutableStateOf(5f) }
    val roundedCornerShape = RoundedCornerShape(16.dp)
    val comment = remember {
        mutableStateOf("")
    }

    Dialog(
        onDismissRequest = { dismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {
        Box(
            modifier = Modifier
                .height(height)
                .width(width)
                .background(AppColor.White, roundedCornerShape)
                .clip(roundedCornerShape)
                .border(1.dp, AppColor.BlueViolet)
                .imePadding()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                RatingBar(value = rating, onValueChange = {
                    rating = it
                }, onRatingChanged = {
                    Log.d("Sülo", "$rating")
                }, config = RatingBarConfig()
                    .size(starWidth)
                    .stepSize(StepSize.HALF)
                    .style(RatingBarStyle.HighLighted)
                )
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedTextField(
                    value = comment.value,
                    onValueChange = {
                        comment.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(outlinedTextHeight),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high)
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {

                        },
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.9f)
                            .height(buttonHeight),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = AppColor.Heliotrope)
                    ) {
                        Text(text = "Gönder", color = AppColor.White)
                    }
                }
            }
        }
    }
}
