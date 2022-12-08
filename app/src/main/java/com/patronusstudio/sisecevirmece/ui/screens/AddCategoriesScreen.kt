package com.patronusstudio.sisecevirmece.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.viewModels.AddCategoriesScreenViewModel
import com.patronusstudio.sisecevirmece.ui.theme.Beaver
import com.patronusstudio.sisecevirmece.ui.theme.BlueViolet
import com.patronusstudio.sisecevirmece.ui.theme.Purple200
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

data class QuestionModel(
    override var id: Int,
    var question: String
) : BaseModelWithIndex() {
    override fun equals(other: Any?): Boolean = false
}

abstract class BaseModelWithIndex {
    abstract var id: Int
}

@Composable
@Preview
fun AddCategoriesScreen() {
    val width = LocalConfiguration.current.screenWidthDp

    val viewModel = hiltViewModel<AddCategoriesScreenViewModel>()
    var btnAddLocationX by remember { mutableStateOf(0.dp) }
    val offsetStateAddBtn =
        animateDpAsState(targetValue = btnAddLocationX, animationSpec = tween(1000))
    var btnRemoveLocationX by remember { mutableStateOf(0.dp) }
    val offsetStateRemoveBtn =
        animateDpAsState(targetValue = btnRemoveLocationX, animationSpec = tween(1000))

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val list = viewModel.questionList.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueViolet)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            AddImage((width * 0.3).dp)
            CategoryName((width * 0.5).dp, 60.dp)
        }
        GetCardWithConstraint(questions = {
            LazyColumn(content = {
                this.items(list) { item ->
                    QuestionViewItem(questionModel = item, valueChange = {
                        viewModel.updateQuestionModelText(item, it)
                    }, removeBtnClicked = {
                        viewModel.removeQuestionModel(item)
                        if (viewModel.questionList.value.size <= 5) {
                            btnAddLocationX = 0.dp
                            btnRemoveLocationX = 0.dp
                        } else {
                            btnAddLocationX = (-80).dp
                            btnRemoveLocationX = 80.dp
                        }
                    })
                    if(item.id == list.last().id){
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }, state = listState)
        }, butons = {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .offset(x = offsetStateAddBtn.value)
            ) {
                CircleImageButton(id = R.drawable.add) {
                    viewModel.addNewQuestionModel()
                    if (list.size > 5) {
                        btnAddLocationX = (-80).dp
                        btnRemoveLocationX = 80.dp
                    } else {
                        btnAddLocationX = 0.dp
                        btnRemoveLocationX = 0.dp
                    }
                    coroutineScope.launch {
                        listState.scrollToItem(list.size)
                    }
                }
            }
            if (list.size > 5) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .offset(x = offsetStateRemoveBtn.value)
                ) {
                    CircleImageButton(id = R.drawable.save) {

                    }
                }
            }
        })
    }
}

@Composable
private fun CircleImageButton(@DrawableRes id: Int, clicked: () -> Unit) {
    Image(
        painter = painterResource(id = id),
        contentDescription = "", modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .border(1.dp, Color.White, CircleShape)
            .clickable {
                clicked()
            }
    )

}


@Composable
fun AddImage(size: Dp) {
    val cornerShape16 = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .size(size)
            .clip(cornerShape16)
            .border(2.dp, Purple200, cornerShape16)
            .background(Color.White), contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.category),
            contentDescription = "", modifier = Modifier.size(80.dp)
        )
    }
}

@Composable
fun CategoryName(width: Dp, height: Dp) {
    val constantName = "Paket İsmi"
    val packageName = remember { mutableStateOf(constantName) }
    val cornerShape8 = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(cornerShape8)
            .border(2.dp, Color.Green, cornerShape8)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(value = packageName.value,
            singleLine = true, onValueChange = {
                packageName.value = it
            }, modifier = Modifier
                .onFocusEvent {
                    if (it.isFocused) {
                        if (packageName.value == constantName)
                            packageName.value = ""
                    } else {
                        if (packageName.value == "")
                            packageName.value = constantName
                    }
                }
                .padding(8.dp)
        )
    }
}

@Composable
fun QuestionViewItem(
    modifier: Modifier = Modifier, questionModel: QuestionModel,
    valueChange: (String) -> Unit, removeBtnClicked: () -> Unit
) {
    val deviceWithSize = LocalConfiguration.current.screenWidthDp
    val widthSize = (deviceWithSize * 0.9).dp
    val textFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Purple200
    )
    Row(
        modifier = modifier
            .width(widthSize)
            .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = questionModel.id.toString(),
            modifier = modifier.fillMaxWidth(0.05f),
            style = TextStyle().copy(textAlign = TextAlign.Center)
        )
        TextField(
            value = questionModel.question,
            onValueChange = {
                valueChange(it)
            },
            placeholder = {
                Text(text = "Sorunuzu girin.")
            },
            colors = textFieldColors,
            singleLine = true,
            modifier = modifier.fillMaxWidth(0.7f),

            )
        Box(
            modifier = modifier
                .fillMaxSize(0.2f)
                .clip(CircleShape)
                .background(Color.White)
                .clickable {
                    removeBtnClicked()
                },
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(id = R.drawable.error), contentDescription = "",modifier.fillMaxSize())
        }
    }
}

@Composable
fun GetCardWithConstraint(questions: @Composable () -> Unit, butons: @Composable () -> Unit) {
    val localHeight = LocalConfiguration.current.screenHeightDp * 0.8
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val (cardRef, addBtnRef) = createRefs()
        Box(
            modifier = Modifier
                .requiredHeightIn(100.dp, localHeight.dp)
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(16.dp))
                .background(Beaver, RoundedCornerShape(16.dp))
                .animateContentSize(tween(500))
                .constrainAs(cardRef) {
                    this.top.linkTo(parent.top)
                    this.centerHorizontallyTo(parent)
                }
        ) {
            Box(
                contentAlignment = Alignment.TopCenter
            ) {
                questions()
            }
        }
        Box(
            modifier = Modifier
                .constrainAs(addBtnRef) {
                    this.top.linkTo(cardRef.bottom)
                    this.bottom.linkTo(cardRef.bottom)
                }
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            butons()
        }
    }
}
