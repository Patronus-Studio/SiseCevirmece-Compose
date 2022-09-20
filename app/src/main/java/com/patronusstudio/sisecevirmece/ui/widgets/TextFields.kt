package com.patronusstudio.sisecevirmece.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import com.patronusstudio.sisecevirmece.ui.theme.Purple700

@Composable
fun CustomTextField(
        widthSize: Dp,
        textFieldColors: TextFieldColors,
        hintText: String,
        changedText: String,
        onValueChange: (String) -> Unit,
        @DrawableRes trailingIcon: Int? = null,
        trailingIconListener: (() -> Unit)? = null,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        maxLine: Int = 1,
        isError: Boolean = false) {

    OutlinedTextField(
            value = changedText,
            onValueChange = onValueChange,
            placeholder = {
                AnimatedVisibility(visible = changedText.isEmpty()) {
                    Text(text = hintText)
                }
            },
            trailingIcon = if (trailingIcon == null) null else {
                {
                    val modifier = Modifier
                    if (trailingIconListener != null) {
                        modifier.clickable {
                            trailingIconListener()
                        }
                    }
                    Image(painter = painterResource(id = trailingIcon), contentDescription = "star",
                            modifier = modifier)
                }
            },
            modifier = Modifier.width(widthSize),
            colors = textFieldColors, visualTransformation = visualTransformation,
            maxLines = maxLine, isError = isError,
    )
}

@Composable
fun getTextFieldColor(): TextFieldColors {
    return TextFieldDefaults.outlinedTextFieldColors(backgroundColor = Color.White,
            focusedBorderColor = Color.Green, unfocusedBorderColor = Purple700,
            errorBorderColor = Color.Red)
}