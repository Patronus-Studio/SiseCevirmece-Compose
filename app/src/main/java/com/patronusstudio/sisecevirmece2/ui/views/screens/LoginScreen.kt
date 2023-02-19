package com.patronusstudio.sisecevirmece2.ui.views.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.patronusstudio.sisecevirmece2.MainApplication
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.data.enums.LoginScreenNavEnums
import com.patronusstudio.sisecevirmece2.data.utils.BetmRounded
import com.patronusstudio.sisecevirmece2.data.utils.hasInternet
import com.patronusstudio.sisecevirmece2.data.viewModels.LoginViewModel
import com.patronusstudio.sisecevirmece2.ui.screens.LoadingAnimation
import com.patronusstudio.sisecevirmece2.ui.theme.AppColor
import com.patronusstudio.sisecevirmece2.ui.widgets.CustomTextField
import com.patronusstudio.sisecevirmece2.ui.widgets.ErrorSheet
import com.patronusstudio.sisecevirmece2.ui.widgets.getTextFieldColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginScreen(goToAnotherScreen: (LoginScreenNavEnums) -> Unit) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val sheet = remember {
        mutableStateOf(false)
    }
    val lottiAnim by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.register_animation))
    val context = LocalContext.current
    val widthSize = LocalConfiguration.current.screenWidthDp
    val heightSize = LocalConfiguration.current.screenHeightDp
    val widthRatio80 = (widthSize * 0.8).dp
    val widthRatio10 = (widthSize * 0.1).dp

    val heightRatio40 = (heightSize * 0.35).dp
    val heightRatio04 = (heightSize * 0.04).dp
    val heightRatio10 = (heightSize * 0.1).dp
    val heightRatio02 = (heightSize * 0.02).dp

    LaunchedEffect(key1 = sheet.value, block = {
        if (sheet.value) sheetState.show()
        else sheetState.hide()
    })

    val isThereError = viewModel.isThereError.collectAsState().value
    LaunchedEffect(key1 = isThereError) {
        if (isThereError.first) {
            sheet.value = true
        }
    }

    val state = viewModel.token.collectAsState().value
    LaunchedEffect(key1 = state) {
        if (hasInternet(context) == true) {
            if (state.isEmpty().not()) {
                withContext(Dispatchers.IO) {
                    viewModel.setUserToken(context)
                }
                MainApplication.authToken = state
                goToAnotherScreen(LoginScreenNavEnums.LOGIN)
            } else {
                viewModel.userTokenControl(context)
            }
        } else {
            viewModel.isThereError.value = Pair(true, "İnternet bağlantısı mevcut değil.")
        }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            ErrorSheet(message = isThereError.second) {
                sheet.value = false
                viewModel.isThereError.value = Pair(false, "")
            }
        }, sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.BlueViolet),
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                LottieAnimation(
                    composition = lottiAnim,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .width(widthRatio80)
                        .height(heightRatio40),
                )
            }
            UsernameView(viewModel.username.collectAsState().value, widthRatio80) { username ->
                viewModel.setUsername(username)
            }
            Spacer(modifier = Modifier.height(heightRatio04))
            Password(
                viewModel.userPassword.collectAsState().value,
                viewModel.isPasswordTrailLocked.collectAsState().value,
                widthRatio80,
                {
                    viewModel.setUserPassword(it)
                },
                {
                    if (viewModel.isPasswordTrailLocked.value) {
                        viewModel.setTrailIconClicked(false)
                    } else {
                        viewModel.setTrailIconClicked(true)
                    }
                })
            Spacer(modifier = Modifier.height(heightRatio10))
            LoginButton(widthRatio80) {
                if (hasInternet(context) == true) {
                    viewModel.loginWithEmailPass()
                } else {
                    viewModel.isThereError.value = Pair(true, "İnternet bağlantısı mevcut değil.")
                }
            }
            Spacer(modifier = Modifier.height(heightRatio02))
            SignInText(widthRatio80, widthRatio10) {
                goToAnotherScreen(LoginScreenNavEnums.REGISTER)
            }
            Spacer(modifier = Modifier.height(heightRatio04))
            AnimatedVisibility(visible = viewModel.isAnimationShow.collectAsState().value) {
                LoadingAnimation()
            }
        }
        PatronusStudio()
    }
}

@Composable
fun UsernameView(
    username: String,
    widthSize: Dp,
    usernameChanged: (String) -> Unit
) {
    val userEmailTrailIcon = remember { mutableStateOf(R.drawable.user) }
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding(), contentAlignment = Alignment.Center
        ) {
            CustomTextField(
                widthSize = widthSize,
                textFieldColors = getTextFieldColor(),
                hintText = stringResource(R.string.username),
                changedText = username,
                onValueChange = {
                    usernameChanged(it)
                },
                trailingIcon = userEmailTrailIcon.value,
            )
        }
    }
}

@Composable
fun Password(
    userPassword: String,
    isLocked: Boolean,
    widthSize: Dp,
    passwordChanged: (String) -> Unit,
    trailClicked: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        CustomTextField(
            widthSize = widthSize,
            textFieldColors = getTextFieldColor(),
            hintText = "Şifre",
            changedText = userPassword,
            onValueChange = {
                passwordChanged(it)
            },
            trailingIcon = if (isLocked) R.drawable.lock else R.drawable.unlock,
            trailingIconListener = {
                trailClicked()
            }, visualTransformation = if (isLocked) PasswordVisualTransformation()
            else VisualTransformation.None, isError = false
        )
    }
}

@Composable
fun LoginButton(widthSize: Dp, clicked: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Card(
            backgroundColor = AppColor.Mustard, modifier = Modifier
                .width(widthSize)
                .clickable {
                    clicked()
                }, shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Giris Yap",
                fontFamily = BetmRounded,
                fontWeight = FontWeight.Medium,
                style = TextStyle(
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                ), color = AppColor.SunsetOrange,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
    }
}

@Composable
fun SignInText(widthSize: Dp, spaceSize: Dp, clicked: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(spaceSize))
        Box(modifier = Modifier.width(widthSize), contentAlignment = Alignment.CenterEnd) {
            Text(text = "Yeni misin? Aramıza katıl.",
                fontFamily = BetmRounded,
                fontWeight = FontWeight.Light,
                style = TextStyle(
                    fontSize = 12.sp
                ),
                color = AppColor.Mustard,
                modifier = Modifier.clickable { clicked() })
        }
        Spacer(modifier = Modifier.width(spaceSize))
    }
}

@Composable
fun PatronusStudio() {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val buttonSize = (screenWidth * 0.35).dp
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Image(
            painter = painterResource(id = R.drawable.patronus_studio),
            contentDescription = "Google girişi",
            modifier = Modifier.size(buttonSize)
        )
    }
}