package com.patronusstudio.sisecevirmece2.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.data.utils.BetmRounded
import com.patronusstudio.sisecevirmece2.ui.theme.AppColor

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
    isError: Boolean = false
) {

    OutlinedTextField(
        value = changedText,
        onValueChange = onValueChange,
        placeholder = {
            AnimatedVisibility(visible = changedText.isEmpty()) {
                Text(text = hintText, color = AppColor.DavysGrey,fontFamily = BetmRounded, fontWeight = FontWeight.Normal)
            }
        },
        trailingIcon = if (trailingIcon == null) null else {
            {
                Image(painter = painterResource(id = trailingIcon), contentDescription = "star",
                    modifier = if (trailingIconListener != null) {
                        Modifier.clickable {
                            trailingIconListener()
                        }
                    } else Modifier)
            }
        },
        modifier = Modifier
            .width(widthSize)
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, AppColor.Beaver, RoundedCornerShape(20.dp))
            .imePadding(),
        colors = textFieldColors,
        visualTransformation = visualTransformation,
        isError = isError,
        maxLines = maxLine,
        singleLine = maxLine == 1,
        textStyle = TextStyle(fontFamily = BetmRounded, fontWeight = FontWeight.Normal, fontSize = 18.sp)
    )
}

@Composable
fun getTextFieldColor(): TextFieldColors {
    return TextFieldDefaults.outlinedTextFieldColors(
        backgroundColor = AppColor.White,
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        errorBorderColor = AppColor.Red,
        textColor = AppColor.DavysGrey
    )
}

private const val TEXT_SCALE_REDUCTION_INTERVAL = 0.9f

@Composable
fun AutoTextSize(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = colorResource(id = R.color.black),
    textAlign: TextAlign = TextAlign.Center,
    textStyle: TextStyle,
    targetTextSizeHeight: TextUnit = textStyle.fontSize,
    maxLines: Int = 1,
) {
    var textSize by remember { mutableStateOf(targetTextSizeHeight) }
    Text(
        modifier = modifier.animateContentSize(),
        text = text,
        color = color,
        textAlign = textAlign,
        fontSize = textSize,
        fontFamily = textStyle.fontFamily,
        fontStyle = textStyle.fontStyle,
        fontWeight = textStyle.fontWeight,
        lineHeight = textStyle.lineHeight,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult ->
            val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1
            if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                textSize = textSize.times(TEXT_SCALE_REDUCTION_INTERVAL)
            }
        },
    )
}