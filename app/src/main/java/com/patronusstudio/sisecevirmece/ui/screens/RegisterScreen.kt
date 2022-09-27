package com.patronusstudio.sisecevirmece.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.utils.checkEmailCorrect
import com.patronusstudio.sisecevirmece.data.viewModels.LoginViewModel
import com.patronusstudio.sisecevirmece.data.viewModels.RegisterViewModel
import com.patronusstudio.sisecevirmece.ui.theme.BlueViolet
import com.patronusstudio.sisecevirmece.ui.widgets.CustomTextField
import com.patronusstudio.sisecevirmece.ui.widgets.getTextFieldColor

@Composable
fun RegisterScreen() {
    val viewModel = viewModel<RegisterViewModel>()
    val widthSize = LocalConfiguration.current.screenWidthDp
    val heightSize = LocalConfiguration.current.screenHeightDp
    val widthRatio80 = (widthSize * 0.8).dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueViolet)
    ) {
        EmailView(viewModel.emailError.collectAsState().value,
            viewModel.userEmail.collectAsState().value, widthRatio80, { email ->
                viewModel.setUserEmail(email)
            }, {
                val result = viewModel.userEmail.value.checkEmailCorrect()
                viewModel.setUserEmailError(result.not())
            })

    }
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
                hintText = "Email adresi",
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
                        text = "Girilen email adresi hatalı", style = TextStyle(
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


