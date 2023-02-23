package com.patronusstudio.sisecevirmece2.ui.views.dialogs

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.patronusstudio.sisecevirmece2.data.enums.AnimMillis
import com.patronusstudio.sisecevirmece2.data.enums.TruthDareEnum
import com.patronusstudio.sisecevirmece2.data.utils.BetmRounded
import com.patronusstudio.sisecevirmece2.ui.theme.AppColor
import kotlinx.coroutines.delay


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TruthDareSelectDialog(
    dissmissed: () -> Unit,
    truthDareSelected: (TruthDareEnum) -> Unit
) {
    val cardWidth = (LocalConfiguration.current.screenWidthDp * 0.44).dp
    val cardHeight = (LocalConfiguration.current.screenHeightDp * 0.3).dp
    val isVisible = remember { mutableStateOf(false) }
    val selectedTruthDareEnum = remember { mutableStateOf(TruthDareEnum.NOT_SELECTED) }
    LaunchedEffect(key1 = Unit, block = {
        delay(100L)
        isVisible.value = true
    })
    LaunchedEffect(key1 = isVisible.value, block = {
        if (isVisible.value.not()) {
            delay(AnimMillis.SHORT.millis.toLong())
            if (selectedTruthDareEnum.value != TruthDareEnum.NOT_SELECTED) {
                truthDareSelected(selectedTruthDareEnum.value)
            } else dissmissed()
        }
    })
    Dialog(
        onDismissRequest = {
            isVisible.value = isVisible.value.not()
        }, properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
        ) {
            AnimatedCard(
                isVisible.value, cardWidth, cardHeight, true, TruthDareEnum.TRUTH
            ) {
                selectedTruthDareEnum.value = it
                isVisible.value = false
            }
            Spacer(modifier = Modifier.height(32.dp))
            AnimatedCard(
                isVisible.value, cardWidth, cardHeight, false, TruthDareEnum.DARE
            ) {
                selectedTruthDareEnum.value = it
                isVisible.value = false
            }
        }
    }
}

@Composable
private fun AnimatedCard(
    isVisible: Boolean,
    cardWidth: Dp,
    cardHeight: Dp,
    isLeftToRight: Boolean,
    truthDareEnum: TruthDareEnum,
    clicked: (TruthDareEnum) -> Unit
) {
    val animDurationMillis = if (isVisible) AnimMillis.LONG.millis else AnimMillis.SHORT.millis
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            initialOffsetX = { if (isLeftToRight.not()) -it else it },
            animationSpec = tween(
                durationMillis = animDurationMillis
            )
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = animDurationMillis, 0
            )
        ), exit = slideOutHorizontally(
            targetOffsetX = { if (isLeftToRight.not()) it else -it },
            animationSpec = tween(
                durationMillis = animDurationMillis
            )
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = animDurationMillis, 0
            )
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            TDCard(cardWidth = cardWidth, cardHeight = cardHeight, truthDareEnum, clicked)
        }
    }
}

@Composable
private fun TDCard(
    cardWidth: Dp, cardHeight: Dp, truthDareEnum: TruthDareEnum,
    clicked: (TruthDareEnum) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .width(cardWidth)
                .height(cardHeight)
                .background(AppColor.Mustard, RoundedCornerShape(16.dp))
                .clickable { clicked(truthDareEnum) }
        ) {
            Column(
                Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(id = truthDareEnum.getImageId()),
                    contentDescription = null
                )
                Text(
                    text = truthDareEnum.getText(LocalContext.current),
                    color = AppColor.SunsetOrange,
                    fontSize = 32.sp,
                    fontFamily = BetmRounded,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}