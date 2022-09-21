package com.patronusstudio.sisecevirmece.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.utils.checkEmailCorrect
import com.patronusstudio.sisecevirmece.ui.theme.BlueViolet
import com.patronusstudio.sisecevirmece.ui.theme.Mustard
import com.patronusstudio.sisecevirmece.ui.theme.SunsetOrange
import com.patronusstudio.sisecevirmece.ui.widgets.CustomTextField
import com.patronusstudio.sisecevirmece.ui.widgets.getTextFieldColor

@Composable
@Preview
fun LoginScreen() {
    val widthSize = LocalConfiguration.current.screenWidthDp
    val heightSize = LocalConfiguration.current.screenHeightDp
    val widthRatio80 = (widthSize * 0.8).dp
    val widthRatio10 = (widthSize * 0.1).dp

    val heightRatio40 = (heightSize * 0.40).dp
    val heightRatio04 = (heightSize * 0.04).dp
    val heightRatio10 = (heightSize * 0.1).dp
    val heightRatio02 = (heightSize * 0.02).dp

    val emailError = remember { mutableStateOf(false) }
    val userEmail = remember { mutableStateOf("") }
    val userPassword = remember { mutableStateOf("") }

    Column(modifier = Modifier
            .fillMaxSize()
            .background(BlueViolet),
            verticalArrangement = Arrangement.Bottom) {
        TopImage(widthRatio80, heightRatio40)
        Spacer(modifier = Modifier.height(heightRatio10))
        Email(emailError, userEmail, widthRatio80)
        Spacer(modifier = Modifier.height(heightRatio04))
        Password(userPassword, widthRatio80)
        Spacer(modifier = Modifier.height(heightRatio04))
        LoginButton(widthRatio80) {

        }
        Spacer(modifier = Modifier.height(heightRatio02))
        SignInText(widthRatio80, widthRatio10) {

        }
        Spacer(modifier = Modifier.height(heightRatio04))
        GoogleSignIn()
        Spacer(modifier = Modifier.height(heightRatio04))
    }
}

@Composable
fun TopImage(widthRatio80: Dp, heightRatio40: Dp) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Image(painter = painterResource(id = R.drawable.ic_undraw_beer_xg5f),
                contentDescription = "Google girişi", contentScale = ContentScale.Fit,
                modifier = Modifier
                        .width(widthRatio80)
                        .height(heightRatio40))
    }
}

@Composable
fun Email(isThereError: MutableState<Boolean>, userEmail: MutableState<String>, widthSize: Dp) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val userEmailTrailIcon = remember { mutableStateOf(R.drawable.mail) }
    Column {
        Box(modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent {
                    if (it.hasFocus.not() && userEmail.value
                                    .isEmpty()
                                    .not()) {
                        checkEmailCorrect(userEmail.value).run {
                            isThereError.value = this.not()
                        }
                    }
                }, contentAlignment = Alignment.Center) {
            CustomTextField(widthSize = widthSize,
                    textFieldColors = getTextFieldColor(),
                    hintText = "Email adresi",
                    changedText = userEmail.value,
                    onValueChange = {
                        userEmail.value = it
                    }, trailingIcon = userEmailTrailIcon.value,
                    isError = isThereError.value)
        }
        AnimatedVisibility(visible = isThereError.value) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.width((screenWidth * 0.11).dp))
                Box(modifier = Modifier.width(widthSize)) {
                    Text(text = "Girilen email adresi hatalı", style = TextStyle(color = Color.Red, fontSize =
                    14.sp))
                }
                Spacer(modifier = Modifier.width((screenWidth * 0.09).dp))
            }
        }
    }
}

@Composable
fun Password(userPassword: MutableState<String>, widthSize: Dp) {
    val isLocked = remember { mutableStateOf(true) }
    val secondTextTrailIcon = remember { mutableStateOf(R.drawable.lock) }
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        CustomTextField(widthSize = widthSize,
                textFieldColors = getTextFieldColor(),
                hintText = "Şifre",
                changedText = userPassword.value,
                onValueChange = {
                    userPassword.value = it
                },
                trailingIcon = secondTextTrailIcon.value,
                trailingIconListener = {
                    if (isLocked.value) {
                        isLocked.value = false
                        secondTextTrailIcon.value = R.drawable.unlocked
                    } else {
                        isLocked.value = true
                        secondTextTrailIcon.value = R.drawable.lock
                    }
                }, visualTransformation = if (isLocked.value) PasswordVisualTransformation()
        else VisualTransformation.None, isError = false)
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
            Text(text = "Giriş Yap", style = TextStyle(fontSize = 24.sp,
                    textAlign = TextAlign.Center, fontWeight = FontWeight.Bold), color = SunsetOrange,
                    modifier = Modifier.padding(vertical = 12.dp))
        }
    }
}

@Composable
fun SignInText(widthSize: Dp, spaceSize: Dp, clicked: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(spaceSize))
        Box(modifier = Modifier.width(widthSize), contentAlignment = Alignment.CenterEnd) {
            Text(text = "Yeni misin? Aramıza katıl.", style = TextStyle(fontSize = 12.sp,
                    fontWeight = FontWeight.Normal), color = Mustard,
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
            Box(modifier = Modifier
                    .height(2.dp)
                    .width(lineSize)
                    .background(Color.LightGray)
                    .align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(spaceSize))
            Box(modifier = Modifier
                    .size(buttonSize)
                    .clip(RoundedCornerShape(100)), contentAlignment = Alignment.Center) {
                Image(painter = painterResource(id = R.drawable.googleicon), contentDescription = "Google girişi",
                        contentScale = ContentScale.Crop, modifier = Modifier.size(buttonSize))
            }
            Spacer(modifier = Modifier.width(spaceSize))
            Box(modifier = Modifier
                    .height(2.dp)
                    .width(lineSize)
                    .background(Color.LightGray)
                    .align(Alignment.CenterVertically))
        }
    }
}