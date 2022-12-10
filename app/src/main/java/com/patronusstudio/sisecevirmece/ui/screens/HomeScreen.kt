package com.patronusstudio.sisecevirmece.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.patronusstudio.sisecevirmece.MainApplication
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.AvatarStatu
import com.patronusstudio.sisecevirmece.data.enums.InAppScreenNavEnums
import com.patronusstudio.sisecevirmece.data.model.AvatarModel
import com.patronusstudio.sisecevirmece.data.viewModels.HomeViewModel
import com.patronusstudio.sisecevirmece.ui.theme.*
import com.patronusstudio.sisecevirmece.ui.widgets.CardImageWithText
import com.patronusstudio.sisecevirmece.ui.widgets.ErrorSheet
import com.patronusstudio.sisecevirmece.ui.widgets.LevelBar
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
    LaunchedEffect(key1 = Unit) {
        withContext(Dispatchers.Main) {
            viewModel.getUserGameInfo(MainApplication.authToken)
        }
        withContext(Dispatchers.IO) {
            viewModel.getAvatars()
            viewModel.getAllLevel()
        }
    }
    LaunchedEffect(
        key1 = viewModel.loginError.collectAsState().value,
        key2 = viewModel.errorMessage.collectAsState().value
    ) {
        if (viewModel.loginError.value.isNotEmpty()) {
            if (sheetState.isVisible.not()) sheetState.show()
        } else sheetState.hide()
    }

    ModalBottomSheetLayout(sheetState = sheetState,
        sheetContent = {
            ErrorSheet(message = viewModel.loginError.collectAsState().value, errorIconClicked = {
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        viewModel.clearAuthToken(mContext)
                    }
                    route(InAppScreenNavEnums.LOGOUT)
                }
            })
            ErrorSheet(message = viewModel.errorMessage.collectAsState().value)
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BlueViolet)
            ) {
                Space(0.02)
                Title()
                Space(0.05)
                UserPicHousting(viewModel)
                Space(0.02)
                Username(viewModel.userGameInfoModel.collectAsState().value?.username ?: "za xd")
                LevelBar(
                    currentStar = viewModel.userGameInfoModel.collectAsState().value?.starCount
                        ?: 0,
                    currentLevel = viewModel.userGameInfoModel.collectAsState().value?.level.toString(),
                    nextLevelNeedStar = viewModel.calculateNextLevelStarSize(
                        viewModel.levels.collectAsState().value ?: listOf()
                    )
                )
                Space(0.03)
                HomeCards(route)
                Space(0.05)
                PlayButton {
                    when (it) {
                        InAppScreenNavEnums.PLAY_GAME -> {
                            Toast.makeText(mContext, "Oyna", Toast.LENGTH_SHORT).show()
                        }
                        InAppScreenNavEnums.LOGOUT -> {
                            CoroutineScope(Dispatchers.Main).launch {
                                withContext(Dispatchers.IO) {
                                    viewModel.clearAuthToken(mContext)
                                }
                                route(InAppScreenNavEnums.LOGOUT)
                            }
                        }
                        else -> ""
                    }
                }
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
private fun UserPicHousting(viewModel: HomeViewModel) {
    val currentImage = remember { mutableStateOf(viewModel.getCurrentAvatar()) }
    val isClicked = remember { mutableStateOf(false) }
    if (isClicked.value) {
        OpenDialog(viewModel) {
            isClicked.value = false
            if (it != null) currentImage.value = it
        }
    }
    UserPic(0.4, viewModel.getCurrentAvatar() ?: AvatarModel(0, "", 1, AvatarStatu.BUYED)) {
        isClicked.value = true
    }
}

@Composable
private fun Username(username: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(text = username, fontSize = 16.sp, style = TextStyle(color = Mustard))
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
            R.drawable.category, stringResource(id = R.string.add_category),
            backgroundColor = Color.White, textColor = Heliotrope,
            borderColor = SeaSerpent, cardSizeWidth, cardSizeHeight,
            imageSize
        ) {
            route(InAppScreenNavEnums.ADD_CATEGORIES)
        }
        CardImageWithText(
            R.drawable.store, stringResource(id = R.string.store), backgroundColor = Mustard,
            imageSize = imageSize, textColor = SunsetOrange, cardSizeWidth = cardSizeWidth,
            cardSizeHeight = cardSizeHeight
        ) {
            route(InAppScreenNavEnums.STORES)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayButton(playGame: (InAppScreenNavEnums) -> Unit) {
    val width = LocalConfiguration.current.screenWidthDp
    val context = LocalContext.current
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Card(
            modifier = Modifier
                .width(width = (width * 0.8).dp)
                .padding(vertical = 16.dp)
                .combinedClickable(onClick = {
                    Toast
                        .makeText(context, "Oyna", Toast.LENGTH_SHORT)
                        .show()
                }, onLongClick = {
                    playGame(InAppScreenNavEnums.LOGOUT)
                }),
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
fun OpenDialog(viewModel: HomeViewModel, dismiss: (AvatarModel?) -> Unit) {
    //AnimatedVisibility(visible = isClicked.value) {
    val width = (LocalConfiguration.current.screenWidthDp * 0.8).dp
    val height = (LocalConfiguration.current.screenHeightDp * 0.6).dp
    val avatarList = viewModel.avatar.collectAsState().value!!
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
                        itemsIndexed(avatarList, itemContent = { position, itemValue ->
                            Box(
                                Modifier
                                    .wrapContentSize()
                                    .padding(vertical = 8.dp)
                            ) {
                                UserPic(ratio = 0.25, avatar = itemValue) {
                                    if (itemValue.buyedStatu == AvatarStatu.BUYED) {
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
