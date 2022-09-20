package com.patronusstudio.sisecevirmece.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.utils.checkEmailCorrect
import com.patronusstudio.sisecevirmece.ui.theme.BlueViolet
import com.patronusstudio.sisecevirmece.ui.widgets.CustomTextField
import com.patronusstudio.sisecevirmece.ui.widgets.getTextFieldColor

@Composable
@Preview
fun LoginScreen() {
    val widthSize = LocalConfiguration.current.screenWidthDp
    val heightSize = LocalConfiguration.current.screenHeightDp
    Column(modifier = Modifier
            .fillMaxSize()
            .background(BlueViolet)) {
        Spacer(modifier = Modifier.height((heightSize * 0.1).dp))
        Email()
        Spacer(modifier = Modifier.height((heightSize * 0.04).dp))
        Password()
    }
}

@Composable
@Preview
fun Email() {
    val widthSize = LocalConfiguration.current.screenWidthDp
    val userEmail = remember { mutableStateOf("") }
    val userEmailTrailIcon = remember { mutableStateOf(R.drawable.mail) }
    val isThereError = remember { mutableStateOf(false) }
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
            CustomTextField(widthSize = (widthSize * 0.8).dp,
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
                Spacer(modifier = Modifier.width((widthSize * 0.11).dp))
                Box(modifier = Modifier.width((widthSize * 0.8).dp)) {
                    Text(text = "Girilen email adresi hatalı", style = TextStyle(color = Color.Red, fontSize =
                    14.sp))
                }
                Spacer(modifier = Modifier.width((widthSize * 0.09).dp))
            }
        }
    }
}

@Composable
fun Password() {
    val widthSize = LocalConfiguration.current.screenWidthDp
    val isLocked = remember { mutableStateOf(true) }
    val secondText = remember { mutableStateOf("") }
    val secondTextTrailIcon = remember { mutableStateOf(R.drawable.lock) }
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        CustomTextField(widthSize = (widthSize * 0.8).dp,
                textFieldColors = getTextFieldColor(),
                hintText = "Şifre",
                changedText = secondText.value,
                onValueChange = {
                    secondText.value = it
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