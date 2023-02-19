package com.patronusstudio.sisecevirmece2.ui.views.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.patronusstudio.sisecevirmece2.MainApplication
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.data.enums.GenderEnum
import com.patronusstudio.sisecevirmece2.data.utils.BetmRounded
import com.patronusstudio.sisecevirmece2.data.utils.checkEmailCorrect
import com.patronusstudio.sisecevirmece2.data.viewModels.RegisterViewModel
import com.patronusstudio.sisecevirmece2.ui.screens.LoadingAnimation
import com.patronusstudio.sisecevirmece2.ui.screens.SampleAnimation
import com.patronusstudio.sisecevirmece2.ui.theme.AppColor
import com.patronusstudio.sisecevirmece2.ui.widgets.CustomTextField
import com.patronusstudio.sisecevirmece2.ui.widgets.ErrorSheet
import com.patronusstudio.sisecevirmece2.ui.widgets.getTextFieldColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RegisterScreen(passToHome: () -> Unit) {
    val viewModel = hiltViewModel<RegisterViewModel>()
    val mContext = LocalContext.current
    val widthSize = LocalConfiguration.current.screenWidthDp
    val heightSize = LocalConfiguration.current.screenHeightDp
    val widthRatio80 = (widthSize * 0.8).dp

    val heightRatio04 = (heightSize * 0.04).dp

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    LaunchedEffect(key1 = viewModel.errorMessage.collectAsState().value) {
        if (viewModel.errorMessage.value != null) {
            if (sheetState.isVisible.not()) sheetState.show()
        } else sheetState.hide()
    }
    LaunchedEffect(key1 = sheetState.currentValue) {
        if (sheetState.currentValue == ModalBottomSheetValue.Hidden) viewModel.clearErrorMessage()
    }
    LaunchedEffect(key1 = viewModel.userToken.collectAsState().value) {
        if (viewModel.userToken.value != null) {
            MainApplication.authToken = viewModel.userToken.value!!
            passToHome()
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            ErrorSheet(message = viewModel.errorMessage.collectAsState().value.toString()) {
                viewModel.clearErrorMessage()
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColor.BlueViolet)
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), contentAlignment = Alignment.Center) {
                    SampleAnimation(R.raw.register)
                }
                EmailView(viewModel.emailError.collectAsState().value,
                    viewModel.userEmail.collectAsState().value, widthRatio80, { email ->
                        viewModel.setUserEmail(email)
                    }, {
                        val result = viewModel.userEmail.value.checkEmailCorrect()
                        viewModel.setUserEmailError(result.not())
                    })
                Spacer(modifier = Modifier.height(heightRatio04))

                UsernameView(username = viewModel.username.collectAsState().value,
                    widthSize = widthRatio80, usernameChanged = {
                        viewModel.setUsername(it)
                    })
                Spacer(modifier = Modifier.height(heightRatio04))
                Password(
                    userPassword = viewModel.userPassword.collectAsState().value,
                    isLocked = viewModel.isLockedPassword.collectAsState().value,
                    widthSize = widthRatio80,
                    passwordChanged = {
                        viewModel.setUserPassword(it)
                    },
                    trailClicked = {
                        viewModel.setIsLockedPassword(viewModel.isLockedPassword.value.not())
                    }
                )
                Spacer(modifier = Modifier.height(heightRatio04))
                GenderPicker(viewModel.selectedGender.collectAsState().value) {
                    viewModel.setSelectedGender(it)
                }
                Spacer(modifier = Modifier.height(heightRatio04))
                RegisterButton(widthRatio80) {
                    viewModel.register(mContext)
                }
                AnimatedVisibility(visible = viewModel.isLoading.collectAsState().value) {
                    LoadingAnimation()
                }
            }
            PatronusStudio()
        }
    )
}

@Composable
fun EmailView(
    isThereError: Boolean,
    userEmail: String,
    widthSize: Dp,
    emailChanged: (String) -> Unit,
    focusChanged: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val userEmailTrailIcon = remember { mutableStateOf(R.drawable.mail) }
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent {
                    if (it.hasFocus.not() && userEmail
                            .isEmpty()
                            .not()
                    ) {
                        focusChanged()
                    }
                }, contentAlignment = Alignment.Center
        ) {
            CustomTextField(
                widthSize = widthSize,
                textFieldColors = getTextFieldColor(),
                hintText = stringResource(R.string.email_address),
                changedText = userEmail,
                onValueChange = {
                    emailChanged(it)
                }, trailingIcon = userEmailTrailIcon.value,
                isError = isThereError
            )
        }
        AnimatedVisibility(visible = isThereError) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.width((screenWidth * 0.11).dp))
                Box(modifier = Modifier.width(widthSize)) {
                    Text(
                        text = stringResource(R.string.entried_email_is_not_correct), style = TextStyle(
                            color = Color.Red, fontSize =
                            14.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.width((screenWidth * 0.09).dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GenderPicker(selectedGender: GenderEnum, clicked: (GenderEnum) -> Unit) {
    val maleColor: Color
    val femaleColor: Color

    when (selectedGender) {
        GenderEnum.FEMALE -> {
            femaleColor = Color.Green
            maleColor = Color.White
        }
        GenderEnum.MALE -> {
            femaleColor = Color.White
            maleColor = Color.Green
        }
        GenderEnum.NONE -> {
            femaleColor = Color.White
            maleColor = Color.White
        }
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(100))
                .background(Color.White)
                .border(border = BorderStroke(2.dp, femaleColor), RoundedCornerShape(100))
                .combinedClickable(onClick = {
                    clicked(GenderEnum.FEMALE)
                })
        ) {
            Image(
                painter = painterResource(id = R.drawable.player_girl),
                contentDescription = "Player Girl"
            )
        }
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(100))
                .background(Color.White)
                .border(border = BorderStroke(2.dp, maleColor), RoundedCornerShape(100))
                .clickable {
                    clicked(GenderEnum.MALE)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.player_man),
                contentDescription = "Player Man"
            )
        }
    }
}

@Composable
fun RegisterButton(widthSize: Dp, clicked: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Card(backgroundColor = AppColor.Mustard, modifier = Modifier
            .width(widthSize)
            .clickable {
                clicked()
            }, shape = RoundedCornerShape(16.dp)) {
            Text(
                text = stringResource(R.string.register), style = TextStyle(
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = BetmRounded,
                    fontWeight = FontWeight.SemiBold
                ), color = AppColor.SunsetOrange,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
    }
}

