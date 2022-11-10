package com.patronusstudio.sisecevirmece.ui.screens

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.patronusstudio.sisecevirmece.MainApplication
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.enums.LoginScreenNavEnums
import com.patronusstudio.sisecevirmece.data.utils.isConnected
import com.patronusstudio.sisecevirmece.data.viewModels.LoginViewModel
import com.patronusstudio.sisecevirmece.ui.theme.BlueViolet
import com.patronusstudio.sisecevirmece.ui.theme.Mustard
import com.patronusstudio.sisecevirmece.ui.theme.SunsetOrange
import com.patronusstudio.sisecevirmece.ui.widgets.CustomTextField
import com.patronusstudio.sisecevirmece.ui.widgets.ErrorSheet
import com.patronusstudio.sisecevirmece.ui.widgets.getTextFieldColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginScreen(goToAnotherScreen: (LoginScreenNavEnums) -> Unit) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val sheet = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val widthSize = LocalConfiguration.current.screenWidthDp
    val heightSize = LocalConfiguration.current.screenHeightDp
    val widthRatio80 = (widthSize * 0.8).dp
    val widthRatio10 = (widthSize * 0.1).dp

    val heightRatio40 = (heightSize * 0.40).dp
    val heightRatio04 = (heightSize * 0.04).dp
    val heightRatio10 = (heightSize * 0.1).dp
    val heightRatio02 = (heightSize * 0.02).dp

    val state = viewModel.token.collectAsState().value
    LaunchedEffect(key1 = state) {
        if(isConnected()){
            if (state.isEmpty().not()) {
                withContext(Dispatchers.IO) {
                    viewModel.setUserToken(context)
                }
                MainApplication.authToken = state
                goToAnotherScreen(LoginScreenNavEnums.LOGIN)
            } else {
                viewModel.userTokenControl(context)
            }
        }
        else sheet.value = true
    }

    LaunchedEffect(key1 = sheet.value, block = {
        if(sheet.value) sheetState.show()
        else sheetState.hide()
    })

    ModalBottomSheetLayout(
        sheetContent = {
            ErrorSheet(message = "İnternet bağlantısı bulunamadı.") {
                sheet.value = false
            }
        }, sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BlueViolet),
            verticalArrangement = Arrangement.Bottom
        ) {
            TopImage(widthRatio80, heightRatio40)
            Spacer(modifier = Modifier.height(heightRatio10))
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
            Spacer(modifier = Modifier.height(heightRatio04))
            LoginButton(widthRatio80) {
                viewModel.loginWithEmailPass()
            }
            Spacer(modifier = Modifier.height(heightRatio02))
            SignInText(widthRatio80, widthRatio10) {
                goToAnotherScreen(LoginScreenNavEnums.REGISTER)
            }
            Spacer(modifier = Modifier.height(heightRatio04))
            GoogleSignIn()
            Spacer(modifier = Modifier.height(heightRatio04))
            AnimatedVisibility(visible = viewModel.isAnimationShow.collectAsState().value) {
                LoadingAnimation()
            }
        }

    }
}

@Composable
fun TopImage(widthRatio80: Dp, heightRatio40: Dp) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.ic_undraw_beer_xg5f),
            contentDescription = "Google girişi", contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(widthRatio80)
                .height(heightRatio40)
        )
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
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            CustomTextField(
                widthSize = widthSize,
                textFieldColors = getTextFieldColor(),
                hintText = "Kullanıcı Adı",
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
        Card(backgroundColor = Mustard, modifier = Modifier
            .width(widthSize)
            .clickable {
                clicked()
            }) {
            Text(
                text = "Giriş Yap", style = TextStyle(
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
                ), color = SunsetOrange,
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
            Text(text = "Yeni misin? Aramıza katıl.", style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            ), color = Mustard,
                modifier = Modifier.clickable { clicked() })
        }
        Spacer(modifier = Modifier.width(spaceSize))
    }
}

@Composable
fun GoogleSignIn() {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val buttonSize = (screenWidth * 0.08).dp
    val lineSize = (screenWidth * 0.31).dp
    val spaceSize = (screenWidth * 0.05).dp
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Row(modifier = Modifier.width((screenWidth * 0.8).dp)) {
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .width(lineSize)
                    .background(Color.LightGray)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(spaceSize))
            Box(
                modifier = Modifier
                    .size(buttonSize)
                    .clip(RoundedCornerShape(100)), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.googleicon),
                    contentDescription = "Google girişi",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(buttonSize)
                )
            }
            Spacer(modifier = Modifier.width(spaceSize))
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .width(lineSize)
                    .background(Color.LightGray)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}